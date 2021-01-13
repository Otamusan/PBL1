package otamusan.pblconnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 送受信する値を管理するクラス
 * @author otamusan
 *
 */
public class DataManagers {
	private Map<Integer, List<Container>> receives;
	private List<Object> sends;

	private int containerSize;

	public DataManagers(int containerSize) {
		this.containerSize = containerSize;
		this.receives = new HashMap<Integer, List<Container>>();
		this.sends = new ArrayList<Object>(containerSize);
		for (int i = 0; i < this.containerSize; i++) {
			this.sends.add(new Object());
		}
	}

	private Optional<List<Container>> getContainers(int playerid) {
		if (this.receives.containsKey(playerid))
			return Optional.of(this.receives.get(playerid));
		return Optional.empty();
	}

	private Optional<Container> getContainer(int containerid, int playerid) {
		if (!this.getContainers(playerid).isPresent())
			return Optional.empty();
		Container container = this.getContainers(playerid).get().get(containerid);
		return Optional.of(container);
	}

	public void update() {
		for (List<Container> containers : this.receives.values()) {
			for (Container container : containers) {
				container.update();
			}
		}
	}

	public void addContainers(int playerid) {
		ArrayList<Container> containers = new ArrayList<Container>(this.containerSize);
		for (int i = 0; i < this.containerSize; i++) {
			containers.add(new Container());
		}
		this.receives.put(playerid, containers);
	}

	public void removeContainers(int playerid) {
		this.receives.remove(playerid);
	}

	public void receive(Object value, int containerid, int playerid) {
		this.getContainer(containerid, playerid).ifPresent(container -> {
			container.put(value);
		});
	}

	public void setSendData(Object value, int containerid) {
		this.sends.set(containerid, value);
	}

	public boolean shouldSend(Object value, int containerid) {
		return !this.sends.get(containerid).equals(value);
	}

	public Optional<Object> getData(int containerid, int playerid) {
		return this.getContainer(containerid, playerid).flatMap(Container::get);
	}

	public boolean isChange(int containerid, int playerid) {
		return this.getContainer(containerid, playerid).map(Container::isChange).orElse(false);
	}

	public boolean checkRecieved(int containerid, int playerid) {
		return this.getContainer(containerid, playerid).map(Container::checkReceived).orElse(false);
	}

	@Override
	public String toString() {
		return this.receives.toString();
	}
}
