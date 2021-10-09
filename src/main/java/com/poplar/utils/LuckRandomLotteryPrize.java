package com.poplar.utils;

import com.poplar.bean.PrizeConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

/**
 * BY Alex CREATED 2021/9/21
 * 根据概率随机抽奖
 */


public class LuckRandomLotteryPrize {

    public static PrizeConfig randomAward(List<PrizeConfig> configList) {
        if (configList == null || configList.size() <= 0) {
            return null;
        }

        if (configList.size() == 1) {
            return configList.get(0);
        }

        //总概率
        double totalProbability = configList.stream().map(PrizeConfig::getProbability).reduce(0.0, Double::sum);
        //计算每个奖品的概率区间
        ArrayList<Double> sortedPrizeProbabilityList = new ArrayList<>(configList.size() + 1);
        //临时总概率
        double tempTotalProbability = 0;
        for (PrizeConfig prizeConfig : configList) {
            tempTotalProbability += prizeConfig.getProbability();
            sortedPrizeProbabilityList.add(tempTotalProbability / totalProbability);
        }
        //产生0-1之间的随机数
        //随机数在那个概率区间内，则是那个奖项
        double random = Math.random();
        //加入到概率区间中，排序后，返回的下标就是抽中的奖品
        sortedPrizeProbabilityList.add(random);
        sortedPrizeProbabilityList.sort(Comparator.comparingDouble(e -> e));
        int index = sortedPrizeProbabilityList.indexOf(random) > configList.size() ? 0 : sortedPrizeProbabilityList.indexOf(random);
        return configList.get(index);
    }

    public static void main(String[] args) {
        List<PrizeConfig> configList = new ArrayList<>();
        configList.add(new PrizeConfig(1, "HUAWEI P50 Pro", new BigDecimal("6488"), 0.2, 10));
        configList.add(new PrizeConfig(2, "IPHONE 13 Pro", new BigDecimal("7999"), 0.2, 10));
        configList.add(new PrizeConfig(3, "HUAWEI MATE40 Pro", new BigDecimal("6288"), 0.2, 10));
        configList.add(new PrizeConfig(4, "HUAWEI MATE30 Pro", new BigDecimal("5988"), 0.2, 10));
        configList.add(new PrizeConfig(5, "IPHONE 12 Pro", new BigDecimal("5288"), 0.2, 10));
        for (int i = 0; i < 10000; i++) {
            PrizeConfig prizeConfig = LuckRandomLotteryPrize.randomAward(configList);
            System.out.println(prizeConfig.getPrizeName());
        }
    }
}
