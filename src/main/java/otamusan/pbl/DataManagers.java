package otamusan.pbl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DataManagers {
	public Map<Integer, List<Container<Object>>> containers;

	private int containerSize;

	public DataManagers(int containerSize) {
		this.containerSize = containerSize;
		this.containers = new HashMap<Integer, List<Container<Object>>>();
	}

	private Optional<List<Container<Object>>> getContainers(int playerid) {
		if (this.containers.containsKey(playerid))
			return Optional.of(this.containers.get(playerid));
		return Optional.empty();
	}

	private Optional<Container<Object>> getContainer(int containerid, int playerid) {
		if (!this.getContainers(playerid).isPresent())
			return Optional.empty();
		Container<Object> opt = this.getContainers(playerid).get().get(containerid);
		return Optional.of(opt);
	}

	public void update() {
		for (List<Container<Object>> containers : this.containers.values()) {
			for (Container<Object> container : containers) {
				container.update();
			}
		}
	}

	public void addContainers(int playerid) {
		ArrayList<Container<Object>> containers = new ArrayList<Container<Object>>(this.containerSize);
		for (int i = 0; i < this.containerSize; i++) {
			containers.add(new Container<Object>());
		}
		this.containers.put(playerid, containers);
	}

	public void receive(Object value, int containerid, int playerid) {
		this.getContainer(containerid, playerid).ifPresent(container -> {
			container.put(value);
		});
	}

	public Optional<Object> getData(int containerid, int playerid) {
		return this.getContainer(containerid, playerid).flatMap(container -> container.get());
	}

	public Boolean isChange(int containerid, int playerid) {
		if (!this.getContainer(containerid, playerid).isPresent())
			return false;
		return this.getContainer(containerid, playerid).get().isChange();
	}

	@Override
	public String toString() {
		return this.containers.toString();
	}
}
