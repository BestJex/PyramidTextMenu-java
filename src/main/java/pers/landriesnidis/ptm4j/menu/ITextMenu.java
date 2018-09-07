package pers.landriesnidis.ptm4j.menu;

import pers.landriesnidis.ptm4j.option.Option;
import pers.landriesnidis.ptm4j.scene.base.ISceneContext;

public interface ITextMenu {
	/**
	 * 添加选项
	 * @param option
	 */
	void addOption(Option option);
	/**
	 * 添加一个文本类型选项
	 * @param keyword
	 * @param content
	 */
	void addTextOption(String keyword, String content);
	/**
	 *  添加子菜单选项
	 * @param keyword
	 * @param classMenu
	 */
	void addMenuOption(String keyword, Class<? extends TextMenu> classMenu);
	/**
	 * 添加可接受参数的菜单选项
	 * @param keyword
	 * @param classMenu
	 */
	void addArgsMenuOption(String keyword, Class<? extends TextMenu> classMenu);
	/**
	 * 添加返回上一层菜单的选项
	 * @param keyword
	 */
	void addBackOption(String keyword);
	/**
	 * 添加重新加载菜单的选择
	 * @param keyword
	 */
	void addReloadOption(String keyword);
	/**
	 * 添加一行不能被选的文本
	 * @param text
	 */
	public void addTextLine(String text);
	/**
	 * 移除选项
	 * @param option
	 */
	void removeOption(Option option);
	/**
	 * 通过关键字移除选项
	 * @param keyword
	 */
	void removeOptionByKeyword(String keyword);
	
	/**
	 * 根据序号获取选项对象
	 * @param index
	 */
	Option getOption(int index);
	/**
	 * 根据关键字获取选项对象
	 * @param optionKeyword
	 */
	Option getOption(String optionKeyword);
	/**
	 * 获取选项组中最后一条选项对象
	 * @return
	 */
	Option getLastOption();
	
	/**
	 * 根据输入内容选择选项
	 * @param text
	 * @return
	 */
	boolean selectOption(String text, ISceneContext context, Object dataTag);
	
	/**
	 * 显示菜单信息
	 */
	void showMenu(ISceneContext context, Object dataTag);
	/**
	 * 显示格式化信息
	 * @param title
	 * @param content
	 * @param menu
	 */
	void showInfo(String title, String content, String menu, ISceneContext context, Object dataTag);
	/**
	 * 显示
	 * @param msg
	 */
	void showMessage(String msg, ISceneContext context, Object dataTag);
	/**
	 * 当接收到用户输入的信息（当菜单设置允许接收信息时才会被执行）
	 * @param text
	 * @return
	 */
	boolean onTextReveived(String text, ISceneContext context, Object dataTag);
	/**
	 * 获取菜单信息读取器
	 * @return
	 */
	TextMenuReader getTextMenuReader();
	
	/**
	 * 当前菜单是否允许显示行号
	 * @return
	 */
	boolean isAllowShowSerialNumber();
	
	/**
	 * 设置当前菜单是否显示行号
	 * @param allowShowSerialNumber
	 */
	void setAllowShowSerialNumber(boolean allowShowSerialNumber);
	
	/**
	 * 当前菜单是否允许接收文本
	 * @return
	 */
	boolean isAllowReveiceText();
	
	/**
	 * 设置当前菜单是否接收文本
	 * @param isAllowReveiceText
	 */
	void setAllowReveiceText(boolean isAllowReveiceText);
	
}
