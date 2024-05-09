public class MyArrayList<E> {
    private Object[] list;
    private int firstFreeIdx;
    private int size;

    public MyArrayList() {
        size = 10;
        firstFreeIdx = 0;
        list = new Object[size];
    }

    public void doubleSize() {
        firstFreeIdx = size;    
        size *= 2;
        Object[] newList = new Object[size];
        for (int i=0; i<list.length; i++){
            newList[i] = list[i];
        }
        list = newList;
    }

    public boolean add(E element) {
        if (firstFreeIdx == (size-1)) {
            doubleSize();
        } 
        
        list[firstFreeIdx] = element;
        firstFreeIdx++;
        return true;
    }

    public void add(int index, E element) {
        if (firstFreeIdx == (size-1)) {
            doubleSize();
        } 
        for (int i = firstFreeIdx; i>index; i--) {
            list[i] = list[i-1];
        }

        list[index] = element;
        firstFreeIdx++;
    }

    

    @SuppressWarnings("unchecked")
    public E get(int idx) {
        return (E)list[idx];
    }

    @SuppressWarnings("unchecked")
    public E remove(int idx) {
        E ele = (E)list[idx];
        for (int i=idx; i<firstFreeIdx; i++) {
            list[i] = list[i+1];
        }
        firstFreeIdx --;

        return ele;
    }
    
    @SuppressWarnings("unchecked")
    public boolean remove(E ele) {
        for (int i=0; i<firstFreeIdx; i++) {
            E tempEle = (E)(list[i]);
            if (tempEle.equals(ele)) {
                remove(i);
                return true;
            }
        }

        return false;
    }

    public void set(int idx, E ele) {
        list[idx] = ele;
    }

    @SuppressWarnings("unchecked")
    public boolean contains(E obj) {
        for (int i = 0; i < firstFreeIdx; i++) {
            E element = (E) list[i];
            if (element.equals(obj)) {
                return true;
            }
        }
        return false;
    }

    
    
    public String toString() {
        String send = "";
        for (int i = 0; i<firstFreeIdx; i++) {
            send += (list[i].toString() + " ");
        }

        return send;
    }


    public int size() {
        return firstFreeIdx;
    }
}
