package class189;

/**
 * 二分查找算法实现
 * 
 * 核心思想：
 * 1. 在有序数组中查找特定元素
 * 2. 每次比较中间元素，根据比较结果缩小搜索范围
 * 3. 时间复杂度：O(log n)，空间复杂度：O(1)
 * 
 * 应用场景：
 * 1. 在有序数组中查找元素
 * 2. 查找插入位置
 * 3. 查找边界值
 * 
 * 工程化考量：
 * 1. 边界条件处理（空数组、单元素数组）
 * 2. 整数溢出处理（使用 left + (right - left) / 2 而不是 (left + right) / 2）
 * 3. 异常输入处理（null数组）
 * 4. 可配置的比较策略
 */
public class Code01_BinarySearch {
    
    /**
     * 基础二分查找
     * 
     * @param nums 有序数组
     * @param target 目标值
     * @return 目标值在数组中的索引，如果不存在则返回-1
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     * 
     * 示例：
     * 输入：nums = [1,2,3,4,5], target = 3
     * 输出：2
     * 
     * 输入：nums = [1,2,3,4,5], target = 6
     * 输出：-1
     */
    public static int binarySearch(int[] nums, int target) {
        // 异常处理
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        int left = 0;
        int right = nums.length - 1;
        
        // 循环条件：left <= right
        while (left <= right) {
            // 防止整数溢出的中点计算方式
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;  // 找到目标值，返回索引
            } else if (nums[mid] < target) {
                left = mid + 1;  // 目标值在右半部分
            } else {
                right = mid - 1; // 目标值在左半部分
            }
        }
        
        return -1;  // 未找到目标值
    }
    
    /**
     * 查找第一个等于目标值的元素
     * 
     * @param nums 有序数组
     * @param target 目标值
     * @return 第一个等于目标值的元素索引，如果不存在则返回-1
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int findFirst(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        int left = 0;
        int right = nums.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                result = mid;    // 记录找到的位置
                right = mid - 1; // 继续在左半部分查找
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 查找最后一个等于目标值的元素
     * 
     * @param nums 有序数组
     * @param target 目标值
     * @return 最后一个等于目标值的元素索引，如果不存在则返回-1
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int findLast(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        int left = 0;
        int right = nums.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                result = mid;   // 记录找到的位置
                left = mid + 1; // 继续在右半部分查找
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 查找第一个大于等于目标值的元素
     * 
     * @param nums 有序数组
     * @param target 目标值
     * @return 第一个大于等于目标值的元素索引，如果不存在则返回数组长度
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int findFirstGreaterOrEqual(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int left = 0;
        int right = nums.length - 1;
        int result = nums.length;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] >= target) {
                result = mid;   // 记录可能的位置
                right = mid - 1; // 继续在左半部分查找
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /**
     * 查找最后一个小于等于目标值的元素
     * 
     * @param nums 有序数组
     * @param target 目标值
     * @return 最后一个小于等于目标值的元素索引，如果不存在则返回-1
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int findLastLessOrEqual(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        int left = 0;
        int right = nums.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] <= target) {
                result = mid;   // 记录可能的位置
                left = mid + 1; // 继续在右半部分查找
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试基础二分查找
        int[] nums1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println("基础二分查找测试：");
        System.out.println("在数组 [1,2,3,4,5,6,7,8,9] 中查找 5: " + binarySearch(nums1, 5));
        System.out.println("在数组 [1,2,3,4,5,6,7,8,9] 中查找 10: " + binarySearch(nums1, 10));
        
        // 测试查找第一个等于目标值的元素
        int[] nums2 = {1, 2, 2, 2, 3, 4, 5};
        System.out.println("\n查找第一个等于目标值的元素测试：");
        System.out.println("在数组 [1,2,2,2,3,4,5] 中查找第一个 2: " + findFirst(nums2, 2));
        
        // 测试查找最后一个等于目标值的元素
        System.out.println("查找最后一个等于目标值的元素测试：");
        System.out.println("在数组 [1,2,2,2,3,4,5] 中查找最后一个 2: " + findLast(nums2, 2));
        
        // 测试查找第一个大于等于目标值的元素
        System.out.println("\n查找第一个大于等于目标值的元素测试：");
        System.out.println("在数组 [1,2,3,4,5] 中查找第一个 >= 3 的元素: " + findFirstGreaterOrEqual(nums1, 3));
        System.out.println("在数组 [1,2,3,4,5] 中查找第一个 >= 6 的元素: " + findFirstGreaterOrEqual(nums1, 6));
        
        // 测试查找最后一个小于等于目标值的元素
        System.out.println("\n查找最后一个小于等于目标值的元素测试：");
        System.out.println("在数组 [1,2,3,4,5] 中查找最后一个 <= 3 的元素: " + findLastLessOrEqual(nums1, 3));
        System.out.println("在数组 [1,2,3,4,5] 中查找最后一个 <= 0 的元素: " + findLastLessOrEqual(nums1, 0));
    }
}