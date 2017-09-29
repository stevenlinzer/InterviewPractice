package structures;

public class MyStack<Key> {
	class Item {
		Key key;
		Item last;
	}
	int size = 0;
	Item item = null;

	public Key pop() {
		if (item == null) return null;
		Item tempItem = item;
		item = item.last;	// move top of the stack to the next one down
		tempItem.last = null;	// remove last pointer from the item
		size--;
		return tempItem.key;
	}

	public void push(Key key) {
		Item temp = new Item();
		temp.key = key;
		temp.last = item;
		item = temp;
		size++;
	}
	public int size() {
		return size;
	}
	public int search(Key key) {
		Item temp = item;
		int retval = 1;
		while (temp != null) {
			if (key.equals(temp.key)) {
				break;
			}
			temp = temp.last;
			retval++;
		}
		return retval;
	}
	public Key poll() {
		if (item == null) return null;
		return item.key;
	}
}
