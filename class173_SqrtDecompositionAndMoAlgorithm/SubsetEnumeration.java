package class175.随机化与复杂度分析;

import java.util.ArrayList;
import java.util.List;

/**
 * 子集枚举算法实现
 * 核心技巧：使用位掩码高效枚举集合的所有子集
 * 时间复杂度：O(2^n)，其中n是元素个数
 */
public class SubsetEnumeration {
    
    /**
     * 枚举mask的所有非空子集
     * 核心公式：for (int sub = mask; sub > 0; sub = (sub - 1) & mask)
     * 
     * @param mask 表示原集合的位掩码
     * @return 所有非空子集的位掩码列表
     */
    public static List<Integer> enumerateAllSubsets(int mask) {
        List<Integer> subsets = new ArrayList<>();
        // 枚举所有非空子集的经典算法
        for (int sub = mask; sub > 0; sub = (sub - 1) & mask) {
            subsets.add(sub);
        }
        return subsets;
    }
    
    /**
     * 枚举大小为k的子集
     * 
     * @param n 集合大小
     * @param k 子集大小
     * @return 所有大小为k的子集的位掩码列表
     */
    public static List<Integer> enumerateSubsetsOfSize(int n, int k) {
        List<Integer> subsets = new ArrayList<>();
        int mask = (1 << k) - 1; // 初始化为k个1
        
        while (mask < (1 << n)) {
            subsets.add(mask);
            
            // Gosper's Hack - 高效生成下一个k元素子集
            int c = mask & -mask;
            int r = mask + c;
            mask = (((r ^ mask) >>> 2) / c) | r;
        }
        
        return subsets;
    }
    
    /**
     * 枚举包含特定元素的子集
     * 
     * @param mask 原集合的位掩码
     * @param requiredElements 必须包含的元素的位掩码
     * @return 包含所有requiredElements的子集的位掩码列表
     */
    public static List<Integer> enumerateSubsetsWithRequired(int mask, int requiredElements) {
        List<Integer> subsets = new ArrayList<>();
        
        // 检查requiredElements是否是mask的子集
        if ((requiredElements & mask) != requiredElements) {
            return subsets; // requiredElements包含mask中没有的元素
        }
        
        // 枚举所有包含requiredElements的子集
        int remaining = mask & (~requiredElements);
        for (int sub = remaining; ; sub = (sub - 1) & remaining) {
            subsets.add(sub | requiredElements);
            if (sub == 0) break;
        }
        
        return subsets;
    }
    
    /**
     * 将位掩码转换为元素索引列表
     * 
     * @param mask 位掩码
     * @return 元素索引列表
     */
    public static List<Integer> maskToIndices(int mask) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            if ((mask & (1 << i)) != 0) {
                indices.add(i);
            }
        }
        return indices;
    }
    
    /**
     * 计算子集的数量
     * 
     * @param mask 位掩码
     * @return 子集数量（包括空集）
     */
    public static int countSubsets(int mask) {
        return 1 << Integer.bitCount(mask);
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 测试1：枚举所有子集
        int mask = 0b1011; // 表示集合{0,1,3}
        System.out.println("原集合(二进制): " + Integer.toBinaryString(mask));
        System.out.println("所有非空子集:");
        List<Integer> subsets = enumerateAllSubsets(mask);
        for (int sub : subsets) {
            System.out.println("  " + Integer.toBinaryString(sub) + " -> " + maskToIndices(sub));
        }
        
        // 测试2：枚举大小为2的子集
        System.out.println("\n大小为2的子集:");
        List<Integer> size2Subsets = enumerateSubsetsOfSize(4, 2);
        for (int sub : size2Subsets) {
            System.out.println("  " + Integer.toBinaryString(sub) + " -> " + maskToIndices(sub));
        }
        
        // 测试3：枚举包含特定元素的子集
        int required = 0b101; // 必须包含元素0和2
        System.out.println("\n包含元素0和2的子集:");
        List<Integer> requiredSubsets = enumerateSubsetsWithRequired(0b1111, required);
        for (int sub : requiredSubsets) {
            System.out.println("  " + Integer.toBinaryString(sub) + " -> " + maskToIndices(sub));
        }
    }
}