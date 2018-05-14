package utils;

import model.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class PageContent<E extends Entity> {

    List<E> content = new ArrayList<>();
    int page;
    int totalPages;
    boolean firstPage;
    boolean lastPage;

    public PageContent() {
    }

    public List<E> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setContent(List<E> content) {
        this.content = content;
    }

    public void setPage(int page) {
        this.page = page;
        recalculateFirstLast(this.page, this.totalPages);
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        recalculateFirstLast(this.page, this.totalPages);
    }

    private void recalculateFirstLast(int page, int totalPages) {
        firstPage = (page == 1);
        lastPage = (page == totalPages);
    }
}
