public class Item {
    private String date;
    private String title;
    private String employee;
    private String type;
    private String description;
    private String priority;
    private String id;
    private String status;

    public Item(String id, String date, String type, String title, String description, String employee, String priority, String status){
        this.id = id;
        this.date = date;
        this.type = type;
        this.title = title;
        this.description = description;
        this.employee = employee;
        this.priority = priority;
        this.status = status;
    }

    public String getid(){
        return this.id;
    }
    public String getDate(){
        return this.date;
    }
    public String getTitle(){
        return this.title;
    }
    public String getEmployees(){
        return this.employee;
    }
    public String getType(){
        return this.type;
    }
    public String getDescription(){
        return this.description;
    }
    public String getPriority(){
        return this.priority;
    }
    
    public String getStatus(){
        return this.status;
    }

    
}
