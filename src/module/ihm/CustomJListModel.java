package module.ihm;

import java.util.Iterator;

import javax.mail.Message;
import javax.swing.DefaultListModel;

import cookie.swipe.application.utils.EventData;
import cookie.swipe.application.utils.LinkedHashSetPriorityQueueObserver;
import cookie.swipe.application.utils.ObservableLinkedHashSetPriorityQueue;

public class CustomJListModel extends DefaultListModel<Message> implements LinkedHashSetPriorityQueueObserver {

	private static final long serialVersionUID = 8376153077264836337L;
	private ObservableLinkedHashSetPriorityQueue list;
	
	public CustomJListModel(ObservableLinkedHashSetPriorityQueue list) {
		this.list = list;
		list.addObserver(this);
//		this.addListDataListener(jList);
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public Message getElementAt(int index) {
		if(getSize() <= index || index < 0) throw new ArrayIndexOutOfBoundsException(); 
		Iterator<Message> it = list.iterator();
		Message element = null;
		while(index-- >= 0 && it.hasNext()) {
			element = it.next();
		}
		return element;
	}

	@Override
	public void update(ObservableLinkedHashSetPriorityQueue o, EventData data) {
		if(!data.hasIndexes()) throw new IllegalArgumentException("No indexex in EventData");
		switch(data.getType()) {
			case REMOVED:
				fireIntervalRemoved(this, data.getBeginIndex(), data.getEndIndex());
				break;
			case ADDED:
				System.out.println(data.getBeginIndex() + " " + data.getEndIndex());
				fireIntervalAdded(this, data.getBeginIndex(), data.getEndIndex());
				break;
			case CHANGED:
				fireContentsChanged(this, data.getBeginIndex(), data.getEndIndex());
				break;
		}
	}	
}
