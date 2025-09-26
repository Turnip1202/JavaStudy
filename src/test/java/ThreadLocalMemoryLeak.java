import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalMemoryLeak {
    private static ThreadLocal<BigObject> threadLocal = new ThreadLocal<>();

    static class BigObject {
        private byte[] data = new byte[10 * 1024 * 1024]; // 10MB
        private String id; // 用于标识对象

        public BigObject(String id) {
            this.id = id;
        }

        @Override
        protected void finalize() throws Throwable {
            // 重写finalize，当对象被GC回收时会打印信息
            System.out.println("BigObject " + id + " 被GC回收了");
            super.finalize();
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(1); // 核心线程长期存活

        // 任务1：设置ThreadLocal但不清理
        executor.submit(() -> {
            try {
                threadLocal.set(new BigObject("OBJ-1")); // 存储大对象
                System.out.println("任务1：设置了BigObject OBJ-1");
            } finally {
                // 故意不调用remove()
//                 threadLocal.remove();
            }
        });

        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        // 清除ThreadLocal的外部引用
        threadLocal = null;

        // 触发GC，观察OBJ-1是否被回收
        System.gc();
        System.out.println("触发GC（第一次）");

        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        // 任务2：复用核心线程，验证OBJ-1是否还存在
        executor.submit(() -> {
            System.out.println("任务2：运行在核心线程中");
            try {
                // 此时threadLocal已为null，调用get()会抛空指针
                System.out.println("任务2：获取到的BigObject：" + threadLocal.get());
            } catch (NullPointerException e) {
                System.out.println("任务2：捕获异常：" + e.getMessage());
            }
        });

        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        // 再次触发GC，OBJ-1仍不会被回收（内存泄露）
        System.gc();
        System.out.println("触发GC（第二次）");

        executor.shutdown();
    }
}
