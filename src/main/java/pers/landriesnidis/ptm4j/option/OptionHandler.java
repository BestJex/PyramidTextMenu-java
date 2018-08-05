package pers.landriesnidis.ptm4j.option;

public interface OptionHandler {
	/**
	 * 执行前的预处理程序
	 * @param text 接收到文本信息
	 * @param optionContext 正在执行的选择项对象
	 * @return 返回true则继续执行选项的触发事件，返回false则不会触发选项的事件
	 */
	boolean preparatoryExecuteHandle(String text,Option optionContext);
}
