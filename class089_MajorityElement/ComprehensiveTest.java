import java.util.*;

/**
 * 水王数相关算法综合测试
 * 包含所有主要的水王数相关算法实现和测试用例
 */
public class ComprehensiveTest {
    
    // 1. 基础水王数问题 (出现次数大于n/2)
    public static int findMajorityElement(int[] nums) {
        int candidate = 0;
        int count = 0;
        
        // Boyer-Moore投票算法第一阶段：找出候选元素
        for (int num : nums) {
            if (count == 0) {
                candidate = num;
                count = 1;
            } else if (num == candidate) {
                count++;
            } else {
                count--;
            }
        }
        
        // 验证候选元素是否真的是水王数
        count = 0;
        for (int num : nums) {
            if (num == candidate) {
                count++;
            }
        }
        
        return count > nums.length / 2 ? candidate : -1;
    }
    
    // 2. 多数元素 II (出现次数大于n/3)
    public static List<Integer> findMajorityElementsII(int[] nums) {
        // 初始化两个候选元素和它们的计数
        int cand1 = 0, cand2 = 0;
        int count1 = 0, count2 = 0;
        
        // 第一遍遍历，找出候选元素
        for (int num : nums) {
            if (count1 > 0 && num == cand1) {
                count1++;
            } else if (count2 > 0 && num == cand2) {
                count2++;
            } else if (count1 == 0) {
                cand1 = num;
                count1 = 1;
            } else if (count2 == 0) {
                cand2 = num;
                count2 = 1;
            } else {
                count1--;
                count2--;
            }
        }
        
        // 第二遍遍历，统计候选元素的真实出现次数
        count1 = 0;
        count2 = 0;
        for (int num : nums) {
            if (num == cand1) {
                count1++;
            } else if (num == cand2) {
                count2++;
            }
        }
        
        // 构造结果列表
        List<Integer> result = new ArrayList<>();
        int n = nums.length;
        if (count1 > n / 3) {
            result.add(cand1);
        }
        if (count2 > n / 3) {
            result.add(cand2);
        }
        
        return result;
    }
    
    // 3. 合法分割的最小下标
    public static int findMinimumIndexValidSplit(List<Integer> nums) {
        // 第一步：使用Boyer-Moore投票算法找出候选元素
        int candidate = 0;
        int count = 0;
        
        // 投票阶段：找出可能的支配元素
        for (int num : nums) {
            if (count == 0) {
                candidate = num;
                count = 1;
            } else if (num == candidate) {
                count++;
            } else {
                count--;
            }
        }
        
        // 第二步：统计候选元素在整个数组中的出现次数
        count = 0;
        for (int num : nums) {
            if (num == candidate) {
                count++;
            }
        }
        
        // 第三步：遍历所有可能的分割点，检查是否满足条件
        int n = nums.size();
        int leftCount = 0; // 左半部分中候选元素的出现次数
        
        // 遍历所有可能的分割点 i (0 <= i < n-1)
        for (int i = 0; i < n - 1; i++) {
            // 更新左半部分中候选元素的出现次数
            if (nums.get(i) == candidate) {
                leftCount++;
            }
            
            // 计算右半部分中候选元素的出现次数
            int rightCount = count - leftCount;
            
            // 检查左半部分是否满足支配元素条件
            boolean leftValid = leftCount * 2 > (i + 1);
            
            // 检查右半部分是否满足支配元素条件
            boolean rightValid = rightCount * 2 > (n - i - 1);
            
            // 如果两部分都满足条件，则找到了有效分割点
            if (leftValid && rightValid) {
                return i;
            }
        }
        
        // 不存在有效分割点
        return -1;
    }
    
    // 4. 出现次数大于n/k的数
    public static List<Integer> findMoreThanNK(int[] nums, int k) {
        int[][] candidates = new int[k-1][2];
        for (int num : nums) {
            updateCandidates(candidates, k-1, num);
        }
        List<Integer> result = new ArrayList<>();
        collectValidCandidates(candidates, k-1, nums, nums.length, result);
        return result;
    }
    
    private static void updateCandidates(int[][] candidates, int k, int num) {
        for (int i = 0; i < k; i++) {
            if (candidates[i][0] == num && candidates[i][1] > 0) {
                candidates[i][1]++;
                return;
            }
        }
        for (int i = 0; i < k; i++) {
            if (candidates[i][1] == 0) {
                candidates[i][0] = num;
                candidates[i][1] = 1;
                return;
            }
        }
        for (int i = 0; i < k; i++) {
            if (candidates[i][1] > 0) {
                candidates[i][1]--;
            }
        }
    }
    
    private static void collectValidCandidates(int[][] candidates, int k, int[] nums, int n, List<Integer> result) {
        for (int i = 0; i < k; i++) {
            if (candidates[i][1] > 0) {
                int candidate = candidates[i][0];
                int count = 0;
                for (int num : nums) {
                    if (candidate == num) {
                        count++;
                    }
                }
                if (count > n / (k + 1)) {
                    result.add(candidate);
                }
            }
        }
    }
    
    // 打印数组的辅助方法
    public static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("]");
    }
    
    public static void printList(List<Integer> list) {
        System.out.print("[");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            if (i < list.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("]");
    }
    
    // 主测试方法
    public static void main(String[] args) {
        System.out.println("=== 水王数相关算法综合测试 ===\n");
        
        // 测试用例1: 基础水王数问题
        System.out.println("1. 基础水王数问题 (出现次数大于n/2):");
        int[] nums1 = {3, 2, 3};
        System.out.print("输入: ");
        printArray(nums1);
        System.out.println();
        System.out.println("输出: " + findMajorityElement(nums1));
        System.out.println();
        
        int[] nums2 = {2, 2, 1, 1, 1, 2, 2};
        System.out.print("输入: ");
        printArray(nums2);
        System.out.println();
        System.out.println("输出: " + findMajorityElement(nums2));
        System.out.println();
        
        // 测试用例2: 多数元素 II
        System.out.println("2. 多数元素 II (出现次数大于n/3):");
        int[] nums3 = {3, 2, 3};
        System.out.print("输入: ");
        printArray(nums3);
        System.out.println();
        System.out.print("输出: ");
        printList(findMajorityElementsII(nums3));
        System.out.println("\n");
        
        int[] nums4 = {1};
        System.out.print("输入: ");
        printArray(nums4);
        System.out.println();
        System.out.print("输出: ");
        printList(findMajorityElementsII(nums4));
        System.out.println("\n");
        
        // 测试用例3: 合法分割的最小下标
        System.out.println("3. 合法分割的最小下标:");
        List<Integer> nums5 = Arrays.asList(1, 2, 2, 2);
        System.out.println("输入: [1, 2, 2, 2]");
        System.out.println("输出: " + findMinimumIndexValidSplit(nums5));
        System.out.println();
        
        List<Integer> nums6 = Arrays.asList(2, 1, 3, 1, 1, 1, 7, 1, 2, 1);
        System.out.println("输入: [2, 1, 3, 1, 1, 1, 7, 1, 2, 1]");
        System.out.println("输出: " + findMinimumIndexValidSplit(nums6));
        System.out.println();
        
        // 测试用例4: 出现次数大于n/k的数
        System.out.println("4. 出现次数大于n/k的数 (k=3):");
        int[] nums7 = {3, 2, 3};
        System.out.print("输入: ");
        printArray(nums7);
        System.out.println();
        System.out.print("输出: ");
        printList(findMoreThanNK(nums7, 3));
        System.out.println("\n");
        
        int[] nums8 = {1, 1, 1, 2, 2, 3, 3, 3, 3, 3, 3};
        System.out.print("输入: ");
        printArray(nums8);
        System.out.println();
        System.out.print("输出: ");
        printList(findMoreThanNK(nums8, 3));
        System.out.println("\n");
        
        System.out.println("=== 测试完成 ===");
    }
}