package com.paratroops.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.paratroops.entity.Team;
import com.paratroops.gui.JSoldier;
import com.paratroops.util.CipherUtils;
import com.paratroops.util.TroopUtils;
import com.paratroops.util.impl.TroopUtilsImpl;

public class TeamDTO {
    /**
     * 队伍：红方/蓝方
     */
    Team team;

    /**
     * 士兵数量
     */
    private int numSoldiers;

    /**
     * 开箱门限
     */
    private int threshold;

    /**
     * 最高军衔人数
     */
    private int numHighestRank;

    /**
     * 装备箱明文
     */
    private int boxKey;

    /**
     * 士兵序列
     */
    private List<JSoldier> soldiers;

    public TeamDTO(Team team, int numSoldiers, int threshold, int numHighestRank, CipherUtils cipherUtils) {
        this.team = team;
        this.numSoldiers = numSoldiers;
        this.threshold = threshold;
        this.numHighestRank = numHighestRank;

        // 生成开箱明文
        Random random = new Random();
        boxKey = random.nextInt(TroopUtilsImpl.BOX_KEY_UPPER_BOUNDS);

        // 生成士兵序列
        soldiers = new ArrayList<JSoldier>(numSoldiers);
        List<Integer> ranks = cipherUtils.genRandomRankList(numSoldiers, JSoldier.HIGHEST_RANK, numHighestRank);
        for (int i=0; i<numSoldiers; ++i) {
            soldiers.add(new JSoldier(ranks.get(i)+1, cipherUtils, 0, 0, team));
        }

        TroopUtils troopUtils = TroopUtilsImpl.getInstance();
        // 分发队友密钥对
        troopUtils.dispatchPublicKeys(soldiers);
        // 分发开箱密钥对
        troopUtils.dispatchBoxKeyPairs(soldiers, threshold, boxKey);
    }

	public List<JSoldier> getJSoldierList() {
		return soldiers;
    }
    
    public int getBoxKey() {
        return boxKey;
    }

    public int getNumSoldiers() {
        return numSoldiers;
    }

    public int getThreshold() {
        return threshold;
    }

    public int getNumHighestRank() {
        return numHighestRank;
    }
}