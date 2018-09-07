package pers.landriesnidis.ptm4j.scene.base;

import pers.landriesnidis.ptm4j.scene.io.SceneReader;
import pers.landriesnidis.ptm4j.scene.io.SceneWirter;

public interface IBasicScene {
	/**
	 * 获取场景信息写入器
	 * @return
	 */
	SceneWirter getSceneWirter();
	
	/**
	 * 设置场景信息读取器
	 * @param reader
	 */
	void setSceneReader(SceneReader reader);
	
	/**
	 * 获取场景的会话环境
	 * @return
	 */
	ISceneContext getSceneContext();
}
