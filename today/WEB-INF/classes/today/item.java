package today;

public class item {

    String txtRecept; // 접수일자
    String txtContent; // 접수내용
    String txtProcess; // 처리현황
    String txtProcessT; // 처리시간


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
