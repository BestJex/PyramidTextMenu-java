package pers.landriesnidis.ptm4j.menu;

import java.util.ArrayList;
import java.util.List;

import pers.landriesnidis.ptm4j.enums.ActionType;
import pers.landriesnidis.ptm4j.menu.events.BackEvent;
import pers.landriesnidis.ptm4j.menu.events.LoadEvent;
import pers.landriesnidis.ptm4j.menu.events.StopEvent;
import pers.landriesnidis.ptm4j.option.Option;
import pers.landriesnidis.ptm4j.scene.Scene;

public class BaseTextMenu implements IMenuIifeCycle, IMenu{

	//所处的场景
	private Scene scene;
	//上一级菜单
	private BaseTextMenu previousMenu;
	//选择项
	private List<Option> options;
	//标题
	private String title = "Menu";
	//文本内容
	private String textContent;
	// 是否允许接收文本（接收非选择项的文本内容，使用后则不能使用序号进行选择）
	private boolean allowReveiceText;
	private boolean allowShowSerialNumber;
	public BaseTextMenu() {
		options = new ArrayList<Option>();
		onCreate();
	}
	
	public void onCreate() {
		
	}

	public void onLoad(LoadEvent e) {
		showMenu();
	}

	public void onStop(StopEvent e) {
		
	}

	public void onBack(BackEvent e) {
		showMenu();
	}

	public void onDestroy() {
		
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public BaseTextMenu getPreviousMenu() {
		return previousMenu;
	}

	public void setPreviousMenu(BaseTextMenu previousMenu) {
		this.previousMenu = previousMenu;
	}

	public void addOption(Option option) {
		options.add(option);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	public String getTextContent() {
		return textContent;
	}

	public boolean isAllowShowSerialNumber() {
		return allowShowSerialNumber;
	}

	public void setAllowShowSerialNumber(boolean allowShowSerialNumber) {
		this.allowShowSerialNumber = allowShowSerialNumber;
	}

	public boolean isAllowReveiceText() {
		return allowReveiceText;
	}

	public void setAllowReveiceText(boolean isAllowReveiceText) {
		this.allowReveiceText = isAllowReveiceText;
	}
	
	public void addTextOption(String keyword, String content) {
		Option option = new Option(this);
		option.setKeyWord(keyword);
		option.setTextContent(content);
		option.setType(ActionType.TEXT);
		options.add(option);
	}

	public void addMenuOption(String keyword, Class<? extends BaseTextMenu> classMenu) {
		Option option = new Option(this);
		option.setKeyWord(keyword);
		option.setMenuClass(classMenu);
		option.setType(ActionType.MENU);
		options.add(option);
	}

	public void addArgsMenuOption(String keyword, Class<? extends BaseTextMenu> classMenu) {
		Option option = new Option(this);
		option.setKeyWord(keyword);
		option.setMenuClass(classMenu);
		option.setType(ActionType.MENU_ARGS);
		options.add(option);
	}

	public void addBackOption(String keyword) {
		Option option = new Option(this);
		option.setKeyWord(keyword);
		option.setType(ActionType.BACK);
		options.add(option);
	}

	public void addReloadOption(String keyword) {
		Option option = new Option(this);
		option.setKeyWord(keyword);
		option.setType(ActionType.RELOAD);
		options.add(option);
	}

	public void removeOption(Option option) {
		options.remove(option);
	}

	public void removeOptionByKeyword(String keyword) {
		for(Option option:options){
			if(option.getKeyWord().equals(keyword)){
				options.remove(option);
				return;
			}
		}
	}

	public Option getOption(int index) {
		if(index<1 || index>options.size()){
			return null;
		}else{
			return options.get(index-1);
		}
	}

	public Option getOption(String optionKeyword) {
		for(Option o:options){
			String kw = o.getKeyWord();
			if(o.getType()==ActionType.MENU_ARGS){
				
				if(kw.contentEquals(optionKeyword.split(" ")[0])){
					return o;
				}
			}else{
				if(kw.contentEquals(optionKeyword)){
					return o;
				}
			}
		}
		return null;
	}

	public void showMenu() {
		StringBuilder textMenu = new StringBuilder();
		// 判断是否显示序号
		if(isAllowShowSerialNumber()){
			int i=1;
			for(Option o:options){
				textMenu.append(String.format(" [%d] %s\n", i++, o.getKeyWord()));
			}
		}else{
			for(Option o:options){
				textMenu.append(String.format(" · %s\n", o.getKeyWord()));
			}
		}
		
		showInfo(getTitle(),getTextContent(),textMenu.toString());
	}

	public void showInfo(String title, String content, String menu) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("[%s]\n", title));
		sb.append(String.format("%s\n", content));
		sb.append("----------\n");
		sb.append(menu);
		showMessage(sb.toString());
	}

	public void showMessage(String msg) {
		getScene().output(msg);
	}

	public boolean selectOption(String text) {
		// 获取关键字与输入内容相符的选项对象
		Option option = this.getOption(text);
		// 判断选项是否存在
		if(option!=null){
			// 若存在则执行相应操作
			option.execute(text);
			return true;
		}else{
			// 若不存在
			// 判断菜单是否允许接收任意输入文本 且文本信息是否有效
			if(isAllowReveiceText() && onTextReveived(text)){
				return true;
			}
			//执行序号
			int index = 0;
			try{
				index = Integer.parseInt(text.trim());
				option = this.getOption(index);
				if(option!=null){
					option.execute(text);
					return true;
				}else{
					return false;
				}
			}catch (Exception e) {
				return false;
			}	
		}
	}

	public boolean onTextReveived(String text) {
		return false;
	}
}
