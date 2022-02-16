package com.mana.mentor.responses;

import java.util.List;

/**
 * @author manatsachinyeruse@gmail.com
 */

public class PageResponseVM<T> {

    List<T> data;
    long total;
    int pageNumber;
    int totalPages;

    public PageResponseVM(List<T> data, long total, int pageNumber, int totalPages) {
        this.data = data;
        this.total = total;

        this.totalPages = totalPages;
    }

    public PageResponseVM() {}

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
