package wsup.task.search;

import java.util.ArrayList;
import java.util.List;

import wsup.db.def.Def;
import wsup.db.def.DefDBHandler;

/**
 * Runnable to insert new def into DB as sys_user.
 *
 * @author xdai2
 * @since Apr 28, 2015
 *
 */
public class NewDefRunnable implements Runnable {

	private String word;	
	private List<String> defList;
	
	private DefDBHandler defHandler;

	public NewDefRunnable(String word, List<String> defList, DefDBHandler defHandler) {
		this.word = word;
		this.defList = defList;
		this.defHandler = defHandler;
	}
	
	@Override
	public void run() {
		List<Def> defDaoList = new ArrayList<Def>();
		for(String def : defList) {
			Def newDef = Def.getSysUserDef(word, def);
			defDaoList.add(newDef);
		}
		
		defHandler.insertAll(defDaoList);
	}
}
