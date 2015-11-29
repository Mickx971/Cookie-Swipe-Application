package cookie.swipe.application.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.function.Predicate;
import javax.mail.Address;
import javax.mail.MessagingException;
import model.CustomMessage;

public class ObservableLinkedHashSetPriorityQueue extends PriorityQueue<CustomMessage> {
	
	private static final long serialVersionUID = 3853067076820469495L;

	public enum ObservableLinkedHashSetPriorityQueueEvent {
		REMOVED,
		ADDED,
		CHANGED
	}
	
	public ObservableLinkedHashSetPriorityQueue(Comparator<? super CustomMessage> comparator) {
		this(new Vector<CustomMessage>(),comparator);
	}
	
	public ObservableLinkedHashSetPriorityQueue(Collection<CustomMessage> collection, Comparator<? super CustomMessage> comparator) {
		super(comparator);
		addAll(collection);
		obs = new Vector<>();
	}
	
	
    private boolean changed = false;
    private Vector<LinkedHashSetPriorityQueueObserver> obs;

    public synchronized void addObserver(LinkedHashSetPriorityQueueObserver o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    public synchronized void deleteObserver(Observer o) {
        obs.removeElement(o);
    }

    public void notifyObservers() {
        notifyObservers(new EventData(ObservableLinkedHashSetPriorityQueueEvent.CHANGED));
    }

    public void notifyObservers(EventData data) {

        Object[] arrLocal;

        //synchronized (this) {

            if (!changed)
                return;
            arrLocal = obs.toArray();
            clearChanged();
        //}

        for (int i = arrLocal.length-1; i>=0; i--)
            ((LinkedHashSetPriorityQueueObserver)arrLocal[i]).update(this, data);
    }

    public synchronized void deleteObservers() {
        obs.removeAllElements();
    }

    protected synchronized void setChanged() {
        changed = true;
    }

    protected synchronized void clearChanged() {
        changed = false;
    }

    public synchronized boolean hasChanged() {
        return changed;
    }

    public synchronized int countObservers() {
        return obs.size();
    }
    
    public int indexOf(Object e) {
    	int size = size();
    	
    	if(size == 0) 
    		return -1;
    	
    	int index = -1;
    	Iterator<CustomMessage> it = iterator();
    	while(it.hasNext()) {
    		if(!it.next().equals(e)) index++;
    		else break;
    	}
    	
    	if(++index == size) 
    		index = -1;
    	
    	return index;
    }
    
	public List<Object> toList() {
		List<Object> all = new ArrayList<>();
		for(Object o : toArray()) {
			all.add(o);
		}
		return all;
	}
	
	@Override
	public boolean add(CustomMessage e) {
		boolean b = false;
		synchronized (this) {
			b = super.add(e);
		}
		return b;
	}
	
	@Override
	public boolean offer(CustomMessage e) {
		if (contains(e)) return false;
		setChanged();
		EventData data = new EventData(ObservableLinkedHashSetPriorityQueueEvent.ADDED, size(), size(), e);
		notifyObservers(data);
		return super.offer(e);
	}
	
	@Override
	public CustomMessage poll() {
		CustomMessage t = null;
		synchronized (this) {
			t = super.poll();
			if(t != null) {
				setChanged();
				EventData data = new EventData(ObservableLinkedHashSetPriorityQueueEvent.REMOVED, indexOf(t), indexOf(t) + 1, t);
				notifyObservers(data);
			}
		}
		return t;
	}
	
	@Override
	public void clear() {
		synchronized (this) {
			if(size() != 0) {
				setChanged();
				List<Object> list = toList();
				EventData data = new EventData(ObservableLinkedHashSetPriorityQueueEvent.REMOVED, 0, list.size(), list);
				notifyObservers(data);
			}
			super.clear();
		}
	}
		
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean hasChanged = false;
		synchronized (this) {
			List<Object> all = toList();
			hasChanged = super.retainAll(c);
			if(hasChanged) {
				all.removeAll(c);
				for(Object o : all) {
					setChanged();
					EventData data = new EventData(ObservableLinkedHashSetPriorityQueueEvent.REMOVED, indexOf(o), indexOf(o) + 1, o);
					notifyObservers(data);
				}
			}
		}
		return hasChanged;
	}
	
	@Override
	public boolean removeIf(Predicate<? super CustomMessage> predic) {
		boolean hasChanged = false;
		synchronized (this) {
			List<Object> all = toList();
			hasChanged = super.removeIf(predic);
			if(hasChanged) {
				all.removeAll(toList());
				for(Object o : all) {
					setChanged();
					EventData data = new EventData(ObservableLinkedHashSetPriorityQueueEvent.REMOVED, indexOf(o), indexOf(o) + 1, o);
					notifyObservers(data);
				}
			}
		}
		return hasChanged;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean hasChanged = false;
		synchronized (this) {
			List<Object> all = toList();
			hasChanged = super.removeAll(c);
			if(hasChanged) {
				all.removeAll(toList());
				for(Object o : all) {
					setChanged();
					EventData data = new EventData(ObservableLinkedHashSetPriorityQueueEvent.REMOVED, indexOf(o), indexOf(o) + 1, o);
					notifyObservers(data);
				}
			}
		}
		return hasChanged;
	}
	
	@Override
	public boolean remove(Object o) {
		boolean hasChanged = false;
		synchronized (this) {
			hasChanged = super.remove(o);
			if(hasChanged) {
				setChanged();
				EventData data = new EventData(ObservableLinkedHashSetPriorityQueueEvent.REMOVED, indexOf(o), indexOf(o) + 1, o);
				notifyObservers(data);
			}
		}
		return hasChanged;
	}
	
	@Override
	public CustomMessage remove() {
		CustomMessage e = null;
		synchronized (this) {
			if(size() != 0) {
				setChanged();
				EventData data = new EventData(ObservableLinkedHashSetPriorityQueueEvent.REMOVED, 0, 1, toList().get(0));
				notifyObservers(data);
			}
			e = super.remove();
		}
		return e;
	}
        
        public void clearMailFrom(String sender) throws MessagingException {
            Iterator<CustomMessage> it = iterator();
            CustomMessage message;
            while(it.hasNext()) {
                message = it.next();
                for(Address address : message.getFrom()) {
                    if(address.toString().contains(sender)) {
                        it.remove();
                        break;
                    }
                }
            }
        }
	
}
