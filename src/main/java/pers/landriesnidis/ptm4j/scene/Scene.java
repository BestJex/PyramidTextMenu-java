package pers.landriesnidis.ptm4j.scene;

import java.util.LinkedList;

import pers.landriesnidis.ptm4j.enums.ActionType;
import pers.landriesnidis.ptm4j.menu.TextMenu;
import pers.landriesnidis.ptm4j.menu.TextMenuReader;
import pers.landriesnidis.ptm4j.menu.events.BackEvent;
import pers.landriesnidis.ptm4j.menu.events.LoadEvent;
import pers.landriesnidis.ptm4j.menu.events.StartEvent;
import pers.landriesnidis.ptm4j.menu.events.StopEvent;
import pers.landriesnidis.ptm4j.option.Option;
import pers.landriesnidis.ptm4j.scene.io.SceneReader;
import pers.landriesnidis.ptm4j.scene.io.SceneWirter;

public class Scene implements IScene, SceneWirter, SceneReader {

//	// 根目录
//	private TextMenu rootMenu;
//	// 当前运行中的目录
//	private TextMenu runningMenu;
	
	private LinkedList<TextMenu> textMenuLinkedList = new LinkedList<TextMenu>();
	
	// 场景信息读取器
	private SceneReader reader;

	public Scene() {
	}

	public Scene(SceneReader reader) {
		this.reader = reader;
	}

	public void startMenu(TextMenu menu, Option option) {
		startMenu(menu, option, null);
	}

	public void startMenu(TextMenu menu, Option option, String[] args) {
		
		// 保存原运行中的Menu
		TextMenu previousMenu = getRunningMenu();
				
		// 避免在菜单组中出现环
		// 如果新跳转的菜单对象存在于菜单组中，则从菜单组中删除后续(包含新菜单对象)
		if(textMenuLinkedList.contains(menu)){
			while(menu != textMenuLinkedList.removeLast());
		}
		
		// 切换Menu
		setRunningMenu(menu);

		// 创建StopEvent对象
		StopEvent stopEvent = new StopEvent();
		stopEvent.setKeyword(option.getKeyWord());

		// 原Menu触发onStop事件
		previousMenu.onStop(stopEvent);

		// 创建LoadEvent对象
		LoadEvent loadEvent = new LoadEvent();
		loadEvent.setActionType(option.getType());
		loadEvent.setKeyword(option.getKeyWord());
		loadEvent.setPreviousMenu(previousMenu);

		// 新Menu触发onLoad事件
		menu.onLoad(loadEvent);

		// 创建StartEvent对象
		StartEvent startEvent = new StartEvent();
		startEvent.setActionType(option.getType());
		startEvent.setArgs(args);

		// 新Menu触发onStart事件
		menu.onStart(startEvent);
	}

	public void returnToPreviousMenu() {
		returnToPreviousMenu(null);
	}

	public void returnToPreviousMenu(Option option) {
		returnToPreviousMenu(option, null);
	}

	public void returnToPreviousMenu(Option option, String[] args) {
		// 检测是否已是根Menu
		if (getRunningMenu() == getRootMenu())
			return;
		
		// 切换至上一级菜单并保存原运行中的Menu
		TextMenu menu = textMenuLinkedList.removeLast();

		// 原Menu触发onDestroy事件
		menu.onDestroy();
		menu = null;

		// 创建BackEvent事件对象
		BackEvent backEvent = new BackEvent();
		if (option != null) {
			backEvent.setKeyword(option.getKeyWord());
			if (args != null) {
				backEvent.setArgs(args);
			}
		}

		// 重回运行状态的Menu触发onBack事件
		getRunningMenu().onBack(backEvent);

		// 创建StartEvent对象
		StartEvent startEvent = new StartEvent();
		if (option != null) {
			startEvent.setActionType(option.getType());
			if (args != null) {
				startEvent.setArgs(args);
			}
		}

		// 新Menu触发onStart事件
		getRunningMenu().onStart(startEvent);
	}

	public void returnToRootMenu(Option option) {
		
		// 如果当前已处于根菜单则什么也不做
		if(textMenuLinkedList.size()<=1)return;
		
		// 切换菜单
		TextMenu menu = getRunningMenu();

		// 原Menu触发onDestroy事件
		menu.onDestroy();
		menu = null;
		
		// 返回至根菜单
		TextMenu rootMenu = getRootMenu();
		textMenuLinkedList.clear();
		textMenuLinkedList.add(rootMenu);

		// 创建BackEvent事件对象
		BackEvent e = new BackEvent();
		e.setKeyword(option.getKeyWord());
		e.setType(ActionType.BACK_ROOT);

		// 根Menu触发onLoad事件
		getRunningMenu().onBack(e);

		// 创建StartEvent对象
		StartEvent startEvent = new StartEvent();
		startEvent.setActionType(option.getType());
		startEvent.setArgs(null);

		// 新Menu触发onStart事件
		getRunningMenu().onStart(startEvent);
	}

	public void reloadMenu(String[] args) {
		// 保存原Menu和上一层Menu，根据原Menu创建新的同类型Menu并替换
		TextMenu oldMenu = textMenuLinkedList.removeLast();
		TextMenu previousMenu = textMenuLinkedList.getLast();
		TextMenu newMenu = Option.createTextMenuObject(oldMenu.getClass());
		setRunningMenu(newMenu);

		// 创建LoadEvent事件对象
		LoadEvent le = new LoadEvent();
		le.setActionType(ActionType.RELOAD);
		le.setKeyword(null);
		le.setPreviousMenu(previousMenu);

		// 新Menu触发onLoad事件
		newMenu.onLoad(le);

		// 创建StartEvent对象
		StartEvent startEvent = new StartEvent();
		startEvent.setActionType(ActionType.RELOAD);
		startEvent.setArgs(null);

		// 新Menu触发onStart事件
		newMenu.onStart(startEvent);
	}

	public TextMenu getRootMenu() {
		return textMenuLinkedList.getFirst();
	}

	public void setRootMenu(TextMenu rootMenu) {
		textMenuLinkedList.clear();
		textMenuLinkedList.add(rootMenu);
		rootMenu.setScene(this);

		rootMenu.onLoad(new LoadEvent());
		rootMenu.onStart(new StartEvent());
	}

	public TextMenu getRunningMenu() {
		return textMenuLinkedList.getLast();
	}

	public void setRunningMenu(TextMenu runningMenu) {
		runningMenu.setScene(this);
		textMenuLinkedList.add(runningMenu);
	}

	public SceneWirter getSceneWirter() {
		return this;
	}

	public boolean input(String text, Object dataTag) {
		return getRunningMenu().selectOption(text, dataTag);
	}
	
	public void output(String text, TextMenuReader textMenuReader, Object dataTag) {
		this.reader.output(text, textMenuReader, dataTag);
	}

	public void setReader(SceneReader reader) {
		this.reader = reader;
	}
}
