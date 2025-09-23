package com.turnip.interview;

/**
 * 面向对象编程三大特性演示
 * 封装、继承、多态的具体实现
 */
public class OOPExample {
    
    public static void main(String[] args) {
        // 封装演示
        demonstrateEncapsulation();
        
        // 继承演示
        demonstrateInheritance();
        
        // 多态演示
        demonstratePolymorphism();
    }
    
    /**
     * 封装演示
     */
    public static void demonstrateEncapsulation() {
        System.out.println("=== 封装演示 ===");
        
        Person person = new Person("张三", 25);
        System.out.println("姓名: " + person.getName());
        System.out.println("年龄: " + person.getAge());
        
        // 通过setter方法控制数据的有效性
        person.setAge(-5); // 无效年龄
        System.out.println("设置无效年龄后: " + person.getAge());
        
        person.setAge(30); // 有效年龄
        System.out.println("设置有效年龄后: " + person.getAge());
    }
    
    /**
     * 继承演示
     */
    public static void demonstrateInheritance() {
        System.out.println("\n=== 继承演示 ===");
        
        Student student = new Student("李四", 20, "S001", "计算机科学");
        student.introduce();
        student.study();
        
        Teacher teacher = new Teacher("王五", 35, "T001", "Java编程");
        teacher.introduce();
        teacher.teach();
    }
    
    /**
     * 多态演示
     */
    public static void demonstratePolymorphism() {
        System.out.println("\n=== 多态演示 ===");
        
        // 多态：父类引用指向子类对象
        Person[] people = {
            new Student("赵六", 22, "S002", "软件工程"),
            new Teacher("孙七", 40, "T002", "数据结构"),
            new Person("普通人", 30)
        };
        
        // 同一个方法调用，不同的实现
        for (Person person : people) {
            person.introduce(); // 调用各自重写的方法
            
            // 类型判断和向下转型
            if (person instanceof Student) {
                ((Student) person).study();
            } else if (person instanceof Teacher) {
                ((Teacher) person).teach();
            }
        }
    }
}

/**
 * 封装示例：Person类
 * 私有属性，公有方法访问
 */
class Person {
    // 私有属性，外部无法直接访问
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        setAge(age); // 使用setter进行验证
    }
    
    // getter方法
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    // setter方法，包含数据验证
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }
    
    public void setAge(int age) {
        if (age >= 0 && age <= 150) {
            this.age = age;
        } else {
            System.out.println("年龄必须在0-150之间");
        }
    }
    
    // 可以被子类重写的方法
    public void introduce() {
        System.out.println("我是" + name + "，今年" + age + "岁");
    }
}

/**
 * 继承示例：Student类继承Person类
 */
class Student extends Person {
    private String studentId;
    private String major;
    
    public Student(String name, int age, String studentId, String major) {
        super(name, age); // 调用父类构造方法
        this.studentId = studentId;
        this.major = major;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public String getMajor() {
        return major;
    }
    
    // 重写父类方法（多态）
    @Override
    public void introduce() {
        super.introduce(); // 调用父类方法
        System.out.println("我是学生，学号：" + studentId + "，专业：" + major);
    }
    
    // 子类特有方法
    public void study() {
        System.out.println(getName() + "正在学习" + major);
    }
}

/**
 * 继承示例：Teacher类继承Person类
 */
class Teacher extends Person {
    private String teacherId;
    private String subject;
    
    public Teacher(String name, int age, String teacherId, String subject) {
        super(name, age);
        this.teacherId = teacherId;
        this.subject = subject;
    }
    
    public String getTeacherId() {
        return teacherId;
    }
    
    public String getSubject() {
        return subject;
    }
    
    // 重写父类方法（多态）
    @Override
    public void introduce() {
        super.introduce();
        System.out.println("我是老师，工号：" + teacherId + "，教授：" + subject);
    }
    
    // 子类特有方法
    public void teach() {
        System.out.println(getName() + "正在教授" + subject);
    }
}