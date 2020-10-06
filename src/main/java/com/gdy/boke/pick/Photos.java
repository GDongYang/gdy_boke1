package com.gdy.boke.pick;


public class Photos {

    private Long total;

    private Long total_pages;

    private String resultsList;

    @Override
    public String toString() {
        return "Photos{" +
                "total=" + total +
                ", total_pages=" + total_pages +
                ", resultsList='" + resultsList + '\'' +
                '}';
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Long total_pages) {
        this.total_pages = total_pages;
    }

    public String getResultsList() {
        return resultsList;
    }

    public void setResultsList(String resultsList) {
        this.resultsList = resultsList;
    }
}
