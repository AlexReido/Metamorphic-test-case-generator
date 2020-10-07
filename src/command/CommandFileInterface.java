package command;

import workers.FileInterface;

public interface CommandFileInterface {
	FileInterface create(String sourceIn, String sourceOut);
	FileInterface create();
}
