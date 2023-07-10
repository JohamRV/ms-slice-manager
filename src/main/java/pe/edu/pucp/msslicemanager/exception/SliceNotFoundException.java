package pe.edu.pucp.msslicemanager.exception;

public class SliceNotFoundException extends RuntimeException{
    public SliceNotFoundException(String message){
        super(message);
    }
}
