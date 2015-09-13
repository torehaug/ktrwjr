package bufferings.ktr.wjr.server.logic;

import java.util.Random;

import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

public class WjrDefaultMethodRunner implements WjrMethodRunner {

	private Random rand;

	public WjrDefaultMethodRunner() {
		rand = new Random();
	}

	public WjrMethodItem runWjrMethod(final WjrMethodItem methodItem) {
		methodItem.setState(State.RUNNING);
		methodItem.setTrace("Working....");
		methodItem.setLog("Log entry....");
		getRandomResult(methodItem);
		return methodItem;
	}

	private void getRandomResult(final WjrMethodItem methodItem) {
		int runTime = randInt(100, 900);
		methodItem.setTime("" + runTime);

		try {
			Thread.sleep(runTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int result = randInt(1, 90);
		if (result < 70) {
			methodItem.setState(State.SUCCESS);
			methodItem.setResults(
					"<h2>" + methodItem.getClassName() + methodItem.getMethodName() + "</h2>" + "<p>Yay</p>");
		} else if ((result > 70) && (result < 80)) {
			methodItem.setState(State.FAILURE);
			methodItem.setTrace("Foo != bar\nFailed");
			methodItem.setResults(
					"<h2>" + methodItem.getClassName() + methodItem.getMethodName() + "</h2>" + "<p>oh no!</p>");

		} else {
			methodItem.setState(State.ERROR);
			methodItem.setTrace("Could not connect: connection refused\nAborting");
			methodItem.setResults(
					"<h2>" + methodItem.getClassName() + methodItem.getMethodName() + "</h2>" + "<p>connection </p>");

		}

	}

	public int randInt(final int min, final int max) {
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

}
