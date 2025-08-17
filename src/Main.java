public class Main {
  public static void main(String[] args) {
    int age = 21;
    double kill_rate = 2.3;
    int year = 2025;
    int quantity = 1;
    String one = "111";

    double price = 19.99;
    double gpa = 3.5;
    double temperature = -12.3;

    char grade = 'A';
    char symbol = '!';
    char currency = '$';

    boolean killed = true;
    boolean dead = true;
    boolean is_assasinated = true;
    boolean is_student = true;

    System.out.println("The year is ");
    System.out.println(Integer.parseInt(one));
    System.out.println("$"+price);

    System.out.println("Are you a student? " + is_student);

    if(is_student) {
      System.out.println("You are a student!");
    } else {
      System.out.println("You are not a student!");
    }

    if(is_assasinated) {
      System.out.println("Congrats to your quest: COMPLETED");
    } else {
      System.out.println("You failed, let me send some cleaners to you.");
    }

    String name = "ladysman734";
    String food = "pizza";
    String email = "youaredead@dead.dead";
    System.out.println(name);
    System.out.println("Your favorite food is: "+food);

    System.out.println("Hello "+ name);
    System.out.println("You are "+age+" years old");
  }
}
