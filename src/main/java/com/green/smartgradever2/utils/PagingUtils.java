package com.green.smartgradever2.utils;


public  class PagingUtils {
    private final int ROW ;

    private int staIdx;
    private int page;
    private int maxPage;
    private int isMore;

    public PagingUtils(){
        this.ROW = 10;
    }

    public PagingUtils(int page, int maxPage) {
        this.ROW=10;
        this.page=page;
        makePage(page,maxPage);
    }
    public PagingUtils(int page, int maxPage,int row) {
        this.ROW=row;
        this.page=page;
        makePage(page,maxPage);
    }

    public  void startIdx(int page){
        int result = page;
        this.staIdx =result*ROW;
    }

    public  void maxPage(int maxPage){
        this.maxPage=((int) Math.ceil((double) maxPage / ROW))-1;
    }

    public void isMore(int maxPage,int page){
        this.isMore= maxPage>page ? 1:0;
    }

    public void makePage(int page,int maxPage){
        startIdx(page);
        maxPage(maxPage);
        isMore(this.maxPage,page);

    }

    public int getStaIdx() {
        return staIdx;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getIsMore() {
        return isMore;
    }

    public int getPage() {
        return page;
    }

    public int getROW() {
        return ROW;
    }
}
