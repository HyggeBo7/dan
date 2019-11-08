package com.dan.utils.lang;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: RandomCombination
 * @createDate: 2019-10-30 14:20.
 * @description: 一串数字多种组合 的和 = sum
 */
public class RandomCombination<T extends RandomCombination.ICombination> {

    //private int frequency = 0;

    public List<List<T>> combinationSumSingle(List<T> dataList, int sum) {
        return combinationSum(dataList, sum, true, false);
    }

    /**
     * 一串数字,不重复使用,多种组合=sum
     * 必须排序
     *
     * @param dataList 数据
     * @param sum      和
     * @param allFlag  是否匹配全部[true:全部,false:单条]
     * @return 组合结果
     */
    public List<List<T>> combinationSum(List<T> dataList, int sum, boolean sortFlag, boolean allFlag) {
        if (dataList != null && dataList.size() > 0) {
            dataList = new ArrayList<>(dataList);
            //必须排序
            if (sortFlag) {
                dataList.sort(new Comparator<T>() {
                    @Override
                    public int compare(T o1, T o2) {
                        int diff = o1.getCombinationValue() - o2.getCombinationValue();
                        if (diff > 0) {
                            return 1;
                        } else if (diff < 0) {
                            return -1;
                        }
                        //相等为0
                        return 0;
                    }
                });
            }
        }
        List<List<T>> resultList = new ArrayList<>();
        if (dataList == null || dataList.size() <= 0) {
            return resultList;
        }
        helper(dataList, sum, 0, new ArrayList<T>(), resultList, allFlag);
        return resultList;
    }

    private void helper(List<T> dataList, int sum, int start, List<T> combinationList, List<List<T>> resultList, boolean allFlag) {
        //System.out.println("frequency:" + (++frequency));
        if (sum < 0) {
            return;
        }
        if (!allFlag && resultList.size() > 0) {
            //System.out.println("resultList.size():" + start + ",sum:" + sum);
            return;
        }
        if (sum == 0) {
            resultList.add(new ArrayList<>(combinationList));
            return;
        }
        for (int i = start; i < dataList.size(); i++) {
            if (i != start && dataList.get(i).getCombinationValue() == dataList.get(i - 1).getCombinationValue()) {
                //去重,一个值只使用一次
                continue;
            }
            combinationList.add(dataList.get(i));
            helper(dataList, sum - dataList.get(i).getCombinationValue(), i + 1, combinationList, resultList, allFlag);
            combinationList.remove(combinationList.size() - 1);
        }
    }

    public interface ICombination {
        /**
         * 组合的累加的值,调用类实现该接口
         *
         * @return 值
         */
        int getCombinationValue();
    }

}