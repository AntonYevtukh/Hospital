package utils;

import model.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class PageContent<E extends Entity> {

    List<E> content = new ArrayList<>();
    int page;
    int totalPages;

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
        return page == 1;
    }

    public boolean isLastPage() {
        return page == totalPages;
    }

    public void setContent(List<E> content) {
        this.content = content;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
