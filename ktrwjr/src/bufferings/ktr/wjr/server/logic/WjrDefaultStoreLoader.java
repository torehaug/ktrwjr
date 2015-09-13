package bufferings.ktr.wjr.server.logic;

import bufferings.ktr.wjr.shared.model.WjrClassItem;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

/**
 * The loader of the WjrStore for a default testcase.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrDefaultStoreLoader extends WjrStoreLoader {

	public WjrStore loadWjrStore(String searchRootDirPath) {

		final WjrStore wjrStore = new WjrStore();
		wjrStore.addClassItem(new WjrClassItem("Case A"));
		wjrStore.addMethodItem(mock("Case A", "Check input"));
		wjrStore.addMethodItem(mock("Case A", "Check input-sequence"));
		wjrStore.addMethodItem(mock("Case A", "Check integration"));
		wjrStore.addMethodItem(mock("Case A", "Check decsion facts"));
		wjrStore.addMethodItem(mock("Case A", "Check decsion outcome"));

		wjrStore.addClassItem(new WjrClassItem("Case B"));
		wjrStore.addMethodItem(mock("Case B", "Check input"));
		wjrStore.addMethodItem(mock("Case B", "Check input-sequence"));
		wjrStore.addMethodItem(mock("Case B", "Check integration"));
		wjrStore.addMethodItem(mock("Case B", "Check decsion facts"));
		wjrStore.addMethodItem(mock("Case B", "Check decsion outcome"));

		wjrStore.addClassItem(new WjrClassItem("Case C"));
		wjrStore.addMethodItem(mock("Case C", "Check input"));
		wjrStore.addMethodItem(mock("Case C", "Check input-sequence"));
		wjrStore.addMethodItem(mock("Case C", "Check integration"));
		wjrStore.addMethodItem(mock("Case C", "Check decsion facts"));
		wjrStore.addMethodItem(mock("Case C", "Check decsion outcome"));

		wjrStore.addClassItem(new WjrClassItem("Case D"));
		wjrStore.addMethodItem(mock("Case D", "Check input"));
		wjrStore.addMethodItem(mock("Case D", "Check input-sequence"));
		wjrStore.addMethodItem(mock("Case D", "Check integration"));
		wjrStore.addMethodItem(mock("Case D", "Check decsion facts"));
		wjrStore.addMethodItem(mock("Case D", "Check decsion outcome"));

		wjrStore.updateAllSummaries();
		return wjrStore;
	}

	private WjrMethodItem mock(final String className, final String methodName) {
		final WjrMethodItem item = new WjrMethodItem(className, methodName, "WjrDefaultMethodRunner");
		item.setResults("");
		return item;

	}

	@Override
	protected void checkAndStoreTestClass(WjrStore store, Class<?> clazz) {
		throw new RuntimeException("checkAndStoreTestClass not supported for WjrDefaultStoreLoader");
	}

}
