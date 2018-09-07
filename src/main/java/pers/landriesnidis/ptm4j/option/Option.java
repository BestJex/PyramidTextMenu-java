package pers.landriesnidis.ptm4j.option;

import pers.landriesnidis.ptm4j.enums.ActionType;
import pers.landriesnidis.ptm4j.menu.TextMenu;
import pers.landriesnidis.ptm4j.scene.base.ISceneContext;

public class Option implements IOption {
	// 触发动作的关键字
	private String keyWord;
	// 动作类型
	private ActionType type;
	// 所属的菜单对象
	private TextMenu menuContext;
	// 文本类信息（对应ActionType:TEXT）
	private String textContent;
	// 触发的下级菜单（仅对应ActionType:MENU,MENU_ARGS）
	private Class<? extends TextMenu> menuClass;
	// 准备执行的处理程序
	private OptionHandler preparatoryExecuteHandler;
	// 是否可选
	private Boolean optional = true;

	public Option(TextMenu menuContext) {
		this.menuContext = menuContext;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public Class<? extends TextMenu> getMenuClass() {
		return menuClass;
	}

	public void setMenuClass(Class<? extends TextMenu> menuClass) {
		this.menuClass = menuClass;
	}

	public TextMenu getMenuContext() {
		return menuContext;
	}
	
	public void setPreparatoryExecuteHandler(OptionHandler preparatoryExecuteHandler) {
		this.preparatoryExecuteHandler = preparatoryExecuteHandler;
	}
	
	public OptionHandler getPreparatoryExecuteHandler() {
		return preparatoryExecuteHandler;
	}
	
	public Boolean getOptional() {
		return optional;
	}
	
	public void setOptional(Boolean optional) {
		this.optional = optional;
	}

	public void execute(String text, ISceneContext context, Object dataTag) {
		// 获取所处菜单
		TextMenu menu = this.getMenuContext();
		// 检查可用状态
		if(!this.optional)return;
		// 执行预处理回调方法
		if(this.preparatoryExecuteHandler!=null && !this.preparatoryExecuteHandler.preparatoryExecuteHandle(text,dataTag, this)) return;
		// 根据动作类型执行操作
		switch (getType()) {
		case TEXT:
			if(getTextContent()!=null)context.output(getTextContent(), menu.getTextMenuReader(), context, dataTag);
			break;
		case MENU:
			context.startMenu(createTextMenuObject(getMenuClass()), this);
			break;
		case MENU_ARGS:
			context.startMenu(createTextMenuObject(getMenuClass()), this, text.split(" "));
			break;
		case BACK:
			context.returnToPreviousMenu(this);
			break;
		case BACK_ROOT:
			context.returnToRootMenu(this);
			break;
		case RELOAD:
			context.reloadMenu(text.split(" "));
			break;
		default:
			
		}
	}
	
	public static TextMenu createTextMenuObject(Class<? extends TextMenu> menuClass){
		try {
			try {
				return (TextMenu) Class.forName(menuClass.getName()).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
