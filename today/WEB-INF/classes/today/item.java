package today;

public class item {

    String txtRecept; // ��������
    String txtContent; // ��������
    String txtProcess; // ó����Ȳ
    String txtProcessT; // ó���ð�


    public String getDate() {
        return txtRecept;
    }

    public String getContent() {
        return txtContent;
    }

    public String getProcess() {
        return txtProcess;
    }

    public String getProcessT() {
        return txtProcessT;
    }
    
    public void setDate(String date) {
        this.txtRecept = date;
    }

    public void setContent(String txtContent) {
        this.txtContent = txtContent;
    }

    public void setProcess(String txtProcess) {
        this.txtProcess = txtProcess;
    }

    public void setProcessT(String txtProcessT) {
        this.txtProcessT = txtProcessT;
    }
    
}
