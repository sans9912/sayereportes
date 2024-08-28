package app.empresa.ferreland.utidades;

public class APIError {

    private String[] errors;

    public String[] getErrors() {
        return errors;
    }

    public void setErrors(String[] errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ClassPojo [errors = " + errors + "]";
    }
}