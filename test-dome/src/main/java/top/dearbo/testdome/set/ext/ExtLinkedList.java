package top.dearbo.testdome.set.ext;

/**
 * @fileName: ExtLinkedList
 * @author: Dan
 * @createDate: 2018-07-09 13:16.
 * @description: 手写LinkedList
 */
public class ExtLinkedList<E> {

    /**
     * 长度-大小
     */
    private int size;

    /**
     * 第一个元素
     */
    private Node first;

    /**
     * 最后一个元素
     */
    private Node last;

    class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

    }

    public boolean add(E e) {
        Node<E> node = new Node(last, e, null);
        //如果第一个元素为null,说明是第一个
        if (first == null) {
            first = node;
        } else {
            //否则2+
            last.next = node;
        }
        last = node;
        size++;
        return true;
    }

    public E get(int index) {
        //验证下标
        checkElementIndex(index);
        return getNode(index).item;
    }

    public boolean remove(int index) {
        //验证下标
        checkElementIndex(index);
        //获取删除元素节点
        Node<E> oldNode = getNode(index);
        if (oldNode != null) {
            Node<E> oldPrevNode = oldNode.prev;
            Node<E> oldNextNode = oldNode.next;
            //说明是第一个节点
            if (oldPrevNode == null) {
                first = oldNextNode;
            } else {
                oldPrevNode.next = oldNextNode;
                oldNode.prev = null;
            }
            //说明是最后一个节点
            if (oldNextNode == null) {
                last = oldPrevNode;
            } else {
                oldNextNode.prev = oldPrevNode;
                oldNode.next = null;
            }
            //进行回收
            oldNode.item = null;
            size--;
            return true;
        }
        return false;
    }

    public void add(int index, E e) {

        checkPositionIndex(index);

        //下标和长度相等，说明是在最后追加
        if (index == size) {
            Node<E> addNode = new Node<>(last, e, null);

            //说明还未添加节点
            if (last == null) {
                first = addNode;
            } else {
                last.next = addNode;
            }
            last = addNode;

        } else {

            Node<E> oldNode = getNode(index);
            Node<E> prev = oldNode.prev;

            Node<E> addNode = new Node<>(prev, e, oldNode);
            oldNode.prev = addNode;

            //说明是插入到第一个节点下
            if (prev == null) {
                first = addNode;
            } else {
                prev.next = addNode;
            }
        }
        size++;
    }

    private Node<E> getNode(int index) {
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
            return x;
        }
    }

    private void checkElementIndex(int index) {
        if (!isElementIndex(index)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    public int size() {
        return this.size;
    }

    public static void main(String[] args) {
        ExtLinkedList<String> extLinkedList = new ExtLinkedList();
        extLinkedList.add("A");
        extLinkedList.add("B");
        extLinkedList.add("C");
        System.out.println("旧数据size:" + extLinkedList.size());
        for (int i = 0; i < extLinkedList.size(); i++) {
            System.out.println(i + "===i===" + extLinkedList.get(i));
        }
        extLinkedList.remove(0);
        System.out.println("删除后size:" + extLinkedList.size());

        extLinkedList.add(extLinkedList.size, "A");
        System.out.println("根据下标添加后:" + extLinkedList.size());
        for (int i = 0; i < extLinkedList.size(); i++) {
            System.out.println(i + "===i===" + extLinkedList.get(i));
        }

    }

}
