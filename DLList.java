public class DLList<E>{
	private Node<E> head, tail;
	private int size;
	
	public DLList(){
		size = 0;
		head = new Node<E>(null);
		tail = new Node<E>(null);
		
		head.setNext(tail);
		tail.setPrev(head);
		head.setPrev(null);
		tail.setNext(null);
	}
	
	public void add(E newElement){
		Node<E> newNode = new Node<E>(newElement);
		Node<E> current = head;
		
		for (int i = 0; i < size + 1; i++){
			if (current.next().equals(tail)){
				current.setNext(newNode);
				newNode.setPrev(current);
				
				newNode.setNext(tail);
				tail.setPrev(newNode);
			}
			
			current = current.next();
		}
		
		size++;
	}
	
	public E get(int location){
		Node<E> current = head.next();
		
		for (int i = 0; i < size; i++){
			if (i == location){
				return current.get();
			}
			current = current.next();
		}
		
		return null;
	}
	
	public void remove(E removeElement){
		Node<E> current = head.next();
		
		for (int i = 0; i < size; i++){
			if (current.get().equals(removeElement)){
				Node<E> before = current.prev();
				Node<E> after = current.next();
				
				before.setNext(after);
				after.setPrev(before);
				size--;
			}
		
			current = current.next();
		}
	}
	
	public void remove(int location){
		Node<E> current = head.next();
		
		for (int i = 0; i < size; i++){
			if (i == location){
				Node<E> before = current.prev();
				Node<E> after = current.next();
				
				before.setNext(after);
				after.setPrev(before);
				size--;
			}
		
			current = current.next();
		}
	}
	
	public String toString(){
		String allElements = "";
		
		Node<E> current = head.next();
		
		for (int i = 0; i < size; i++){
			allElements = allElements + current.get();
			
			if (!current.next().equals(tail)){
				allElements = allElements + "\n";
			}
			
			current = current.next();
		}
		
		return allElements;
	}
	
	public int size(){
		return size;
	}

    private Node<E> getNode(int index) {
        Node<E> current;
        if (index <= size/2) {
            current = head.next();
            for (int j=0; j<index; j++) {
                current = current.next();
            }
        } else {
            current = tail.prev();
            for (int j=size-1; j>index; j--) {
                current = current.prev();
            }
        }

        return current;

    }

    public void set(int index, E element) {
        Node<E> node = getNode(index);
        node.set(element);
    }

    public void clear() {
        head.setNext(tail);
        tail.setPrev(head);
        size = 0; 
    }

}
