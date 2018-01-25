package logic;

public class ExceptionHandler {

    //TODO: consider using object of the class and implement a constructor
    //TODO: implement some prompts for gui

    public static void generalExceptionHandler(Exception e, String customMessage){
        System.out.println(customMessage);
        System.out.println(e.getMessage());
        System.out.println(e.getStackTrace());
    }
}