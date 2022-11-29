public class Item {
    private String date;
    private String title;
    private String employee;
    private String type;
    private String description;
    private String priority;
    private String id;

    public Item(String id, String date, String type, String title, String description, String employee, String priority){
        this.id = id;
        this.date = date;
        this.type = type;
        this.title = title;
        this.description = description;
        this.employee = employee;
        this.priority = priority;
    }

    public String getid(){
        return id;
    }
    public String getDate(){
        return date;
    }
    public String getTitle(){
        return title;
    }
    public String getEmployees(){
        return employee;
    }
    public String getType(){
        return type;
    }
    public String getDescription(){
        return description;
    }
    public String getPriority(){
        return priority;
    }

    
}
