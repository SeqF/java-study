package linkedlist.base;

/**
 * 单链表节点:
 * 1、数据域
 * 2、指针域
 */
public class Node {

    public int data;

    public Node next;

    public Node(){
    }

    public Node(int data){
        this.data=data;
    }

    public Node(int data,Node next){
        this.data=data;
        this.next=next;
    }

}
