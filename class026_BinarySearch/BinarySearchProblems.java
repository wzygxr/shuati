import java.util.*;

/**
 * 二分查找算法实现与相关题目解答 (Java版本)
 * 
 * 本文件包含了二分查找的各种实现和在不同场景下的应用
 * 涵盖了LeetCode、Codeforces、SPOJ等平台的经典题目
 */
public class BinarySearchProblems {
    
    /**
     * 基础二分查找
     * 在有序数组中查找目标值
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * @param nums 有序数组
     * @param target 目标值
     * @return 目标值的索引，如果不存在返回-1
     */
    public static int search(int[] nums, int target) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            // 使用位运算避免整数溢出
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
    
    /**
     * 查找插入位置
     * 在有序数组中查找目标值应该插入的位置
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * @param nums 有序数组
     * @param target 目标值
     * @return 插入位置索引
     */
    public static int searchInsert(int[] nums, int target) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int left = 0, right = nums.length;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
    
    /**
     * 查找元素的第一个和最后一个位置
     * 在有序数组中查找目标值的起始和结束位置
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * @param nums 有序数组
     * @param target 目标值
     * @return [起始位置, 结束位置]，如果不存在返回[-1, -1]
     */
    public static int[] searchRange(int[] nums, int target) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return new int[]{-1, -1};
        }
        
        int first = findLeft(nums, target);
        // 如果找不到>=target的元素，或者该元素不等于target，则说明target不存在
        if (first == -1 || nums[first] != target) {
            return new int[]{-1, -1};
        }
        
        int last = findRight(nums, target);
        return new int[]{first, last};
    }
    
    /**
     * 辅助方法：查找>=target的最左位置
     */
    private static int findLeft(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        int ans = -1;
        while (l <= r) {
            int m = l + ((r - l) >> 1);
            if (nums[m] >= target) {
                ans = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return ans;
    }
    
    /**
     * 辅助方法：查找<=target的最右位置
     */
    private static int findRight(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        int ans = -1;
        while (l <= r) {
            int m = l + ((r - l) >> 1);
            if (nums[m] <= target) {
                ans = m;
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return ans;
    }
    
    /**
     * 有效的完全平方数
     * 判断一个数是否是完全平方数
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * @param num 正整数
     * @return 如果是完全平方数返回true，否则返回false
     */
    public static boolean isPerfectSquare(int num) {
        // 边界条件检查
        if (num < 1) {
            return false;
        }
        if (num == 1) {
            return true;
        }
        
        long l = 1, r = num / 2; // 一个数的平方根不会超过它的一半(除了1)
        while (l <= r) {
            long m = l + ((r - l) >> 1);
            long square = m * m;
            if (square == num) {
                return true;
            } else if (square > num) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return false;
    }
    
    /**
     * 第一个错误的版本
     * 查找第一个错误的版本
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * @param n 版本总数
     * @return 第一个错误的版本号
     */
    public static int firstBadVersion(int n) {
        int left = 1, right = n;
        while (left < right) {
            // 假设存在 isBadVersion API
            int mid = left + ((right - left) >> 1);
            if (isBadVersion(mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
    
    /**
     * 模拟API：判断版本是否错误
     */
    private static boolean isBadVersion(int version) {
        // 这里只是一个示例实现
        // 实际应用中会根据具体需求实现
        return version >= 4; // 假设第4个版本开始是错误的
    }
    
    /**
     * 寻找峰值元素
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * @param nums 数组
     * @return 峰值元素的索引
     */
    public static int findPeakElement(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] > nums[mid + 1]) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
    
    /**
     * 寻找旋转排序数组中的最小值
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * @param nums 旋转排序数组
     * @return 数组中的最小值
     */
    public static int findMin(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < nums[right]) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return nums[left];
    }
    
    /**
     * 搜索二维矩阵
     * 
     * 时间复杂度: O(log(m*n))
     * 空间复杂度: O(1)
     * 
     * @param matrix 二维矩阵
     * @param target 目标值
     * @return 如果找到目标值返回true，否则返回false
     */
    public static boolean searchMatrix(int[][] matrix, int target) {
        // 边界条件检查
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        
        int m = matrix.length, n = matrix[0].length;
        int left = 0, right = m * n - 1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            int midValue = matrix[mid / n][mid % n];
            
            if (midValue == target) {
                return true;
            } else if (midValue < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return false;
    }
    
    /**
     * 搜索旋转排序数组
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * @param nums 旋转排序数组
     * @param target 目标值
     * @return 目标值的索引，如果不存在返回-1
     */
    public static int searchInRotatedSortedArray(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            if (nums[mid] == target) {
                return mid;
            }
            
            // 左半部分有序
            if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } 
            // 右半部分有序
            else {
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return -1;
    }
    
    /**
     * 寻找重复数
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(1)
     * 
     * @param nums 包含重复数字的数组
     * @return 重复的数字
     */
    public static int findDuplicate(int[] nums) {
        int left = 1, right = nums.length - 1;
        
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            int count = 0;
            
            // 计算小于等于mid的数字个数
            for (int num : nums) {
                if (num <= mid) {
                    count++;
                }
            }
            
            // 根据抽屉原理判断重复数字在哪一侧
            if (count > mid) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    /**
     * Codeforces 706B. Interesting drink
     * 
     * 时间复杂度: O(n log n + q log n)
     * 空间复杂度: O(1)
     * 
     * @param prices 饮料价格数组
     * @param queries 查询金额数组
     * @return 每个查询可以购买的饮料种类数
     */
    public static int[] howManyDrinks(int[] prices, int[] queries) {
        Arrays.sort(prices);
        int[] result = new int[queries.length];
        
        for (int i = 0; i < queries.length; i++) {
            int money = queries[i];
            int left = 0, right = prices.length;
            
            while (left < right) {
                int mid = left + ((right - left) >> 1);
                if (prices[mid] <= money) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            result[i] = left;
        }
        
        return result;
    }
    
    /**
     * SPOJ - AGGRCOW (Aggressive cows)
     * 
     * 时间复杂度: O(n log(max-min) * n)
     * 空间复杂度: O(1)
     * 
     * @param positions 摊位位置数组
     * @param cows 奶牛数量
     * @return 最大化最小距离
     */
    public static int aggressiveCows(int[] positions, int cows) {
        Arrays.sort(positions);
        int left = 0, right = positions[positions.length - 1] - positions[0];
        int result = 0;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (canPlaceCows(positions, cows, mid)) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 辅助方法：检查是否能以给定最小距离放置奶牛
     */
    private static boolean canPlaceCows(int[] positions, int cows, int minDist) {
        int count = 1;
        int lastPosition = positions[0];
        
        for (int i = 1; i < positions.length; i++) {
            if (positions[i] - lastPosition >= minDist) {
                count++;
                lastPosition = positions[i];
                if (count == cows) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 带异常处理的安全二分查找
     * 
     * @param nums 有序数组
     * @param target 目标值
     * @return 目标值的索引，如果不存在返回-1
     * @throws IllegalArgumentException 当输入参数非法时抛出异常
     */
    public static int safeBinarySearch(int[] nums, int target) {
        // 检查输入参数
        if (nums == null) {
            throw new IllegalArgumentException("数组不能为null");
        }
        
        if (nums.length == 0) {
            return -1;
        }
        
        int left = 0, right = nums.length - 1;
        
        // 检查数组是否已排序
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < nums[i-1]) {
                throw new IllegalArgumentException("数组必须是已排序的");
            }
        }
        
        while (left <= right) {
            // 防止整数溢出的中点计算
            int mid = left + ((right - left) >> 1);
            
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1;
    }
    
    /**
     * 性能优化版本的二分查找
     * 
     * @param nums 有序数组
     * @param target 目标值
     * @return 目标值的索引，如果不存在返回-1
     */
    public static int optimizedBinarySearch(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        
        while (left <= right) {
            // 使用无符号右移避免溢出
            int mid = (left + right) >>> 1;
            
            if (nums[mid] == target) {
                return mid;
            }
            
            // 使用位运算优化比较
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1;
    }
    
    /**
     * LeetCode 69. Sqrt(x)
     * 
     * 时间复杂度: O(log x)
     * 空间复杂度: O(1)
     * 
     * @param x 输入的非负整数
     * @return x 的平方根的整数部分
     */
    public static int mySqrt(int x) {
        if (x == 0 || x == 1) {
            return x;
        }
        
        int left = 1, right = x;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 防止整数溢出，使用除法而不是乘法
            if (mid > x / mid) {
                right = mid - 1;
            } else {
                // 如果下一个值会溢出或者大于x/mid，则当前mid是最大的有效平方根
                if (mid + 1 > x / (mid + 1)) {
                    return mid;
                }
                left = mid + 1;
            }
        }
        
        return right;
    }
    
    /**
     * LeetCode 374. 猜数字大小
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * @param n 数字范围上限
     * @return 猜中的数字
     */
    public static int guessNumber(int n) {
        int left = 1, right = n;
        
        while (left <= right) {
            // 防止整数溢出
            int mid = left + ((right - left) >> 1);
            int result = guess(mid);
            
            if (result == 0) {
                return mid;  // 猜对了
            } else if (result == 1) {
                left = mid + 1;  // 猜小了，往右边找
            } else {
                right = mid - 1;  // 猜大了，往左边找
            }
        }
        
        return -1;  // 理论上不会执行到这里
    }
    
    // 示例的guess方法，实际会由系统提供
    private static int guess(int num) {
        int pick = 6;  // 假设选中的数字是6
        if (num < pick) return 1;
        else if (num > pick) return -1;
        else return 0;
    }
    
    /**
     * LeetCode 875. 爱吃香蕉的珂珂
     * 
     * 时间复杂度: O(n log maxPile)
     * 空间复杂度: O(1)
     * 
     * @param piles 香蕉堆数组
     * @param h 警卫离开的时间（小时）
     * @return 最小的吃香蕉速度
     */
    public static int minEatingSpeed(int[] piles, int h) {
        int left = 1;
        int right = 0;
        
        // 找到最大的香蕉堆，作为右边界
        for (int pile : piles) {
            right = Math.max(right, pile);
        }
        
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            
            if (canFinish(piles, h, mid)) {
                right = mid;  // 可以吃完，尝试更小的速度
            } else {
                left = mid + 1;  // 不能吃完，需要更大的速度
            }
        }
        
        return left;
    }
    
    /**
     * 辅助方法：判断以速度speed是否能在h小时内吃完所有香蕉
     */
    private static boolean canFinish(int[] piles, int h, int speed) {
        long time = 0;
        
        for (int pile : piles) {
            // 计算吃完当前堆需要的时间
            time += (pile + speed - 1) / speed;  // 等价于 Math.ceil(pile / speed)
            
            // 如果时间已经超过h，可以提前返回false
            if (time > h) {
                return false;
            }
        }
        
        return time <= h;
    }
    
    /**
     * LeetCode 1011. 在D天内送达包裹的能力
     * 
     * 时间复杂度: O(n log totalWeight)
     * 空间复杂度: O(1)
     * 
     * @param weights 包裹重量数组
     * @param days 天数限制
     * @return 船的最小运载能力
     */
    public static int shipWithinDays(int[] weights, int days) {
        int left = 0;  // 最小运载能力：最大的单个包裹重量
        int right = 0; // 最大运载能力：所有包裹的总重量
        
        for (int weight : weights) {
            left = Math.max(left, weight);
            right += weight;
        }
        
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            
            if (canShipInDays(weights, days, mid)) {
                right = mid;  // 可以在days天内运完，尝试更小的运载能力
            } else {
                left = mid + 1;  // 不能在days天内运完，需要更大的运载能力
            }
        }
        
        return left;
    }
    
    /**
     * 辅助方法：判断以capacity的运载能力是否能在days天内运完所有包裹
     */
    private static boolean canShipInDays(int[] weights, int days, int capacity) {
        int currentWeight = 0;
        int dayCount = 1;  // 至少需要1天
        
        for (int weight : weights) {
            // 如果当前包裹的重量已经超过了运载能力，不可能运完
            if (weight > capacity) {
                return false;
            }
            
            // 如果当前累计重量加上当前包裹的重量超过了运载能力，需要新的一天
            if (currentWeight + weight > capacity) {
                dayCount++;
                currentWeight = weight;  // 新的一天从当前包裹开始
                
                // 如果天数已经超过了限制，可以提前返回false
                if (dayCount > days) {
                    return false;
                }
            } else {
                currentWeight += weight;  // 继续往当前天添加包裹
            }
        }
        
        return dayCount <= days;
    }
    
    /**
     * AtCoder ABC023D - 射撃王 (Shooting King)
     * 
     * 时间复杂度: O(N^3 log N)
     * 空间复杂度: O(N)
     * 
     * @param points 目标点数组，每个点是[x, y]
     * @return 最少需要的子弹数
     */
    public static int minBullets(int[][] points) {
        int n = points.length;
        if (n == 0) return 0;
        if (n == 1) return 1;
        
        int left = 1, right = n;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (canCover(points, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
    
    /**
     * 辅助方法：判断是否可以用k条直线覆盖所有点
     */
    private static boolean canCover(int[][] points, int k) {
        boolean[] covered = new boolean[points.length];
        return coverRecursive(points, covered, 0, k);
    }
    
    /**
     * 递归辅助方法：尝试覆盖剩余的点
     */
    private static boolean coverRecursive(int[][] points, boolean[] covered, int coveredCount, int remainingLines) {
        int n = points.length;
        if (coveredCount == n) return true;
        if (remainingLines == 0) return false;
        
        // 找到第一个未覆盖的点
        int first = -1;
        for (int i = 0; i < n; i++) {
            if (!covered[i]) {
                first = i;
                break;
            }
        }
        
        // 尝试通过第一个未覆盖的点画一条直线
        for (int i = 0; i < n; i++) {
            if (i == first || covered[i]) continue;
            
            // 标记所有在这条直线上的点
            boolean[] newCovered = covered.clone();
            int newCount = coveredCount;
            
            for (int j = 0; j < n; j++) {
                if (!newCovered[j] && isCollinear(points[first], points[i], points[j])) {
                    newCovered[j] = true;
                    newCount++;
                }
            }
            
            if (coverRecursive(points, newCovered, newCount, remainingLines - 1)) {
                return true;
            }
        }
        
        // 如果没有其他点，可以单独用一条直线覆盖第一个未覆盖的点
        return coverRecursive(points, covered, coveredCount + 1, remainingLines - 1);
    }
    
    /**
     * 辅助方法：判断三个点是否共线
     */
    private static boolean isCollinear(int[] p1, int[] p2, int[] p3) {
        // 使用叉积判断三点共线
        // (y2 - y1) * (x3 - x1) == (y3 - y1) * (x2 - x1)
        return (p2[1] - p1[1]) * (p3[0] - p1[0]) == (p3[1] - p1[1]) * (p2[0] - p1[0]);
    }

    /**
     * LeetCode 410. 分割数组的最大值
     * 题目来源: https://leetcode.com/problems/split-array-largest-sum/
     * 
     * 题目描述:
     * 给定一个非负整数数组 nums 和一个整数 m，需要将这个数组分成 m 个非空的连续子数组。
     * 设计一个算法使得这 m 个子数组各自和的最大值最小。
     * 
     * 思路分析:
     * 1. 二分答案法：答案范围在[max(nums), sum(nums)]之间
     * 2. 对于每个可能的答案mid，检查能否将数组分成<=m个子数组且每个子数组和<=mid
     * 3. 如果可以，说明答案可能更小，向左搜索；否则向右搜索
     * 
     * 时间复杂度: O(n * log(sum - max))
     * 空间复杂度: O(1)
     * 是否最优解: 是
     * 
     * @param nums 非负整数数组
     * @param m 分割的子数组数量
     * @return 最小的最大子数组和
     */
    public static int splitArray(int[] nums, int m) {
        // 边界检查
        if (nums == null || nums.length == 0 || m <= 0) {
            return 0;
        }
        
        long left = 0;  // 最小可能值：数组中的最大值
        long right = 0; // 最大可能值：数组元素总和
        
        for (int num : nums) {
            left = Math.max(left, num);
            right += num;
        }
        
        while (left < right) {
            long mid = left + ((right - left) >> 1);
            
            // 检查是否能在限制下分成m个或更少的子数组
            if (canSplit(nums, m, mid)) {
                right = mid;  // 可以分割，尝试更小的最大值
            } else {
                left = mid + 1;  // 不能分割，需要更大的最大值
            }
        }
        
        return (int) left;
    }
    
    /**
     * 辅助方法：检查是否能在maxSum限制下将数组分成count个或更少的子数组
     */
    private static boolean canSplit(int[] nums, int count, long maxSum) {
        int splits = 1;  // 至少有一个子数组
        long currentSum = 0;
        
        for (int num : nums) {
            if (currentSum + num > maxSum) {
                splits++;  // 需要新的子数组
                currentSum = num;
                
                if (splits > count) {
                    return false;  // 超过了允许的子数组数量
                }
            } else {
                currentSum += num;
            }
        }
        
        return true;
    }
    
    /**
     * LeetCode 4. 寻找两个正序数组的中位数
     * 题目来源: https://leetcode.com/problems/median-of-two-sorted-arrays/
     * 
     * 题目描述:
     * 给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。
     * 请你找出并返回这两个正序数组的中位数。
     * 
     * 思路分析:
     * 1. 使用二分查找在较短的数组上进行分割
     * 2. 确保左半部分的最大值 <= 右半部分的最小值
     * 3. 中位数由左半部分最大值和右半部分最小值决定
     * 
     * 时间复杂度: O(log(min(m,n)))
     * 空间复杂度: O(1)
     * 是否最优解: 是
     * 
     * @param nums1 第一个正序数组
     * @param nums2 第二个正序数组
     * @return 两个数组的中位数
     */
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // 确保nums1是较短的数组
        if (nums1.length > nums2.length) {
            int[] temp = nums1;
            nums1 = nums2;
            nums2 = temp;
        }
        
        int m = nums1.length;
        int n = nums2.length;
        int left = 0, right = m;
        
        while (left <= right) {
            int partition1 = left + ((right - left) >> 1);
            int partition2 = ((m + n + 1) >> 1) - partition1;
            
            int maxLeft1 = (partition1 == 0) ? Integer.MIN_VALUE : nums1[partition1 - 1];
            int minRight1 = (partition1 == m) ? Integer.MAX_VALUE : nums1[partition1];
            
            int maxLeft2 = (partition2 == 0) ? Integer.MIN_VALUE : nums2[partition2 - 1];
            int minRight2 = (partition2 == n) ? Integer.MAX_VALUE : nums2[partition2];
            
            if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
                // 找到正确的分割点
                if ((m + n) % 2 == 0) {
                    return (Math.max(maxLeft1, maxLeft2) + Math.min(minRight1, minRight2)) / 2.0;
                } else {
                    return Math.max(maxLeft1, maxLeft2);
                }
            } else if (maxLeft1 > minRight2) {
                right = partition1 - 1;
            } else {
                left = partition1 + 1;
            }
        }
        
        throw new IllegalArgumentException("输入数组不合法");
    }
    
    /**
     * 牛客/剑指Offer: 数字在排序数组中出现的次数
     * 题目来源: https://www.nowcoder.com/practice/70610bf967994b22bb1c26f9ae901fa2
     * 
     * 题目描述:
     * 统计一个数字在排序数组中出现的次数。
     * 
     * 思路分析:
     * 使用二分查找找到目标值的左右边界，次数 = 右边界 - 左边界 + 1
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 是否最优解: 是
     * 
     * @param nums 排序数组
     * @param target 目标值
     * @return 出现次数
     */
    public static int getNumberOfK(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int first = findLeft(nums, target);
        if (first == -1 || nums[first] != target) {
            return 0;
        }
        
        int last = findRight(nums, target);
        return last - first + 1;
    }
    
    /**
     * LeetCode 540. 有序数组中的单一元素
     * 题目来源: https://leetcode.com/problems/single-element-in-a-sorted-array/
     * 
     * 题目描述:
     * 给定一个只包含整数的有序数组，每个元素都会出现两次，唯有一个数只会出现一次，找出这个数。
     * 
     * 思路分析:
     * 1. 单一元素之前，成对元素的第一个位置是偶数索引
     * 2. 单一元素之后，成对元素的第一个位置是奇数索引
     * 3. 使用二分查找定位单一元素
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 是否最优解: 是
     * 
     * @param nums 有序数组
     * @return 单一元素
     */
    public static int singleNonDuplicate(int[] nums) {
        int left = 0, right = nums.length - 1;
        
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            
            // 确保mid是偶数索引
            if (mid % 2 == 1) {
                mid--;
            }
            
            // 检查成对关系
            if (nums[mid] == nums[mid + 1]) {
                // 单一元素在右侧
                left = mid + 2;
            } else {
                // 单一元素在左侧或就是mid
                right = mid;
            }
        }
        
        return nums[left];
    }
    
    /**
     * LeetCode 1482. 制作 m 束花所需的最少天数
     * 题目来源: https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/
     * 
     * 题目描述:
     * 给你一个整数数组 bloomDay，以及两个整数 m 和 k。
     * 现需要制作 m 束花。制作花束时，需要使用花园中相邻的 k 朵花。
     * 花园中有 n 朵花，第 i 朵花会在 bloomDay[i] 时盛开。
     * 请你返回从花园中摘 m 束花需要等待的最少的天数。如果不能摘到 m 束花则返回 -1。
     * 
     * 思路分析:
     * 1. 二分答案：天数范围在[min(bloomDay), max(bloomDay)]之间
     * 2. 对于每个天数，检查能否制作m束花
     * 3. 贪心验证：从左到右扫描，统计连续k朵已开花的数量
     * 
     * 时间复杂度: O(n * log(max - min))
     * 空间复杂度: O(1)
     * 是否最优解: 是
     * 
     * @param bloomDay 每朵花开放的天数
     * @param m 需要的花束数量
     * @param k 每束花包含的花朵数量
     * @return 最少等待天数
     */
    public static int minDays(int[] bloomDay, int m, int k) {
        // 边界检查：如果花的总数不足以制作m束花，返回-1
        if ((long) m * k > bloomDay.length) {
            return -1;
        }
        
        int left = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        
        // 找到天数范围
        for (int day : bloomDay) {
            left = Math.min(left, day);
            right = Math.max(right, day);
        }
        
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            
            if (canMakeBouquets(bloomDay, m, k, mid)) {
                right = mid;  // 可以制作，尝试更少的天数
            } else {
                left = mid + 1;  // 不能制作，需要更多的天数
            }
        }
        
        return left;
    }
    
    /**
     * 辅助方法：检查在给定天数内能否制作m束花
     */
    private static boolean canMakeBouquets(int[] bloomDay, int m, int k, int day) {
        int bouquets = 0;  // 已制作的花束数
        int flowers = 0;   // 当前连续开放的花朵数
        
        for (int bloom : bloomDay) {
            if (bloom <= day) {
                flowers++;
                if (flowers == k) {
                    bouquets++;
                    flowers = 0;
                    if (bouquets == m) {
                        return true;
                    }
                }
            } else {
                flowers = 0;  // 遇到未开放的花，重置计数
            }
        }
        
        return bouquets >= m;
    }

    /**
     * 牛客网: 旋转数组的最小数字
     * 题目来源: https://www.nowcoder.com/practice/9f3231a991af4f55b95579b44b7a01ba
     * 
     * 题目描述:
     * 把一个数组最开始的若干个元素搬到数组的末尾,我们称之为数组的旋转。
     * 输入一个非递减排序的数组的一个旋转,输出旋转数组的最小元素。
     * 
     * 思路分析:
     * 1. 二分查找法,比较中间元素与右边界元素
     * 2. 如果中间元素小于右边界,最小值在左侧
     * 3. 如果中间元素大于右边界,最小值在右侧
     * 4. 如果相等,右边界左移一位
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 是否最优解: 是
     * 
     * @param nums 旋转数组
     * @return 最小元素
     */
    public static int minNumberInRotateArray(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < nums[right]) right = mid;
            else if (nums[mid] > nums[right]) left = mid + 1;
            else right--;
        }
        return nums[left];
    }
    
    /**
     * acwing: 数的三次方根
     * 题目来源: https://www.acwing.com/problem/content/792/
     * 
     * 题目描述:
     * 给定一个浮点数n,求它的三次方根,结果保留6位小数。
     * 
     * 思路分析:
     * 1. 二分查找法,在[-10000, 10000]范围内搜索
     * 2. 精度控制到1e-8
     * 3. 根据mid^3与n的大小关系调整搜索区间
     * 
     * 时间复杂度: O(log(范围/精度))
     * 空间复杂度: O(1)
     * 是否最优解: 是
     * 
     * @param n 输入浮点数
     * @return 三次方根
     */
    public static double cubeRoot(double n) {
        double left = -10000, right = 10000;
        while (right - left > 1e-8) {
            double mid = (left + right) / 2;
            if (mid * mid * mid >= n) right = mid;
            else left = mid;
        }
        return left;
    }
    
    /**
     * 杭电OJ: 查找最接近的元素
     * 题目来源: http://acm.hdu.edu.cn/showproblem.php?pid=2141
     * 
     * 题目描述:
     * 给定三个数组A、B、C，以及多个查询X。对于每个查询，判断是否存在a∈A, b∈B, c∈C，使得a+b+c=X。
     * 
     * 思路分析:
     * 1. 将A+B的所有可能和存储在一个数组中
     * 2. 对这个和数组进行排序
     * 3. 对于每个查询X，使用二分查找在A+B的和数组中查找是否存在X-c（c∈C）
     * 
     * 时间复杂度: O(L*M + N*log(L*M))
     * 空间复杂度: O(L*M)
     * 是否最优解: 是
     * 
     * @param A 数组A
     * @param B 数组B
     * @param C 数组C
     * @param X 查询值
     * @return 是否存在满足条件的组合
     */
    public static boolean findClosestSum(int[] A, int[] B, int[] C, int X) {
        int n = A.length;
        int[] sumAB = new int[n * n];
        int idx = 0;
        for (int a : A) for (int b : B) sumAB[idx++] = a + b;
        Arrays.sort(sumAB);
        
        for (int c : C) {
            int target = X - c;
            int left = 0, right = sumAB.length - 1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                if (sumAB[mid] == target) return true;
                if (sumAB[mid] < target) left = mid + 1;
                else right = mid - 1;
            }
        }
        return false;
    }
    
    /**
     * POJ: 月度开销
     * 题目来源: http://poj.org/problem?id=3273
     * 
     * 题目描述:
     * FJ需要将连续的N天划分为M个时间段，每个时间段的花费是该时间段内每天花费的总和。
     * 他希望最小化所有时间段中花费的最大值。
     * 
     * 思路分析:
     * 1. 二分答案法：答案范围在[max(expenses), sum(expenses)]之间
     * 2. 对于每个可能的答案mid，检查能否将数组分成<=M个子数组且每个子数组和<=mid
     * 3. 如果可以，说明答案可能更小，向左搜索；否则向右搜索
     * 
     * 时间复杂度: O(n log(sum))
     * 空间复杂度: O(1)
     * 是否最优解: 是
     * 
     * @param expenses 每天的开销数组
     * @param M 划分的时间段数
     * @return 最小的最大开销
     */
    public static int monthlyExpense(int[] expenses, int M) {
        int left = 0, right = 0;
        for (int expense : expenses) {
            left = Math.max(left, expense);
            right += expense;
        }
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (canSplitExpenses(expenses, M, mid)) right = mid;
            else left = mid + 1;
        }
        return left;
    }
    
    private static boolean canSplitExpenses(int[] expenses, int M, int maxSum) {
        int count = 1, currentSum = 0;
        for (int expense : expenses) {
            if (currentSum + expense > maxSum) {
                count++;
                currentSum = expense;
                if (count > M) return false;
            } else currentSum += expense;
        }
        return true;
    }
    
    /**
     * 洛谷: 砍树
     * 题目来源: https://www.luogu.com.cn/problem/P1873
     * 
     * 题目描述:
     * 伐木工人需要砍倒M米高的树木，每棵树的高度不同。
     * 伐木工人使用一个设定高度H的锯子，高于H的树木会被砍下高出的部分。
     * 请帮助伐木工人确定锯子的高度H，使得被砍下的树木总长度刚好等于M。
     * 
     * 思路分析:
     * 1. 二分答案法：高度范围在[0, max(treeHeights)]之间
     * 2. 对于每个候选高度mid，计算能砍下的总长度
     * 3. 如果总长度>=M，可以尝试更高的高度；否则需要降低高度
     * 
     * 时间复杂度: O(n log(maxHeight))
     * 空间复杂度: O(1)
     * 是否最优解: 是
     * 
     * @param trees 树的高度数组
     * @param M 需要砍下的总长度
     * @return 锯子的设定高度
     */
    public static int cutTrees(int[] trees, int M) {
        int left = 0, right = 0;
        for (int tree : trees) right = Math.max(right, tree);
        int result = 0;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            long total = 0;
            for (int tree : trees) if (tree > mid) total += tree - mid;
            if (total >= M) {
                result = mid;
                left = mid + 1;
            } else right = mid - 1;
        }
        return result;
    }
    
    /**
     * USACO: 愤怒的奶牛
     * 题目来源: http://www.usaco.org/index.php?page=viewproblem2&cpid=592
     * 
     * 题目描述:
     * 在一个一维的场地中放置了若干堆干草，奶牛被点燃后会向左右两个方向传播爆炸。
     * 爆炸的传播速度是每秒1单位距离。求最小的爆炸半径R，使得点燃任意一个干草堆都能引爆所有干草堆。
     * 
     * 思路分析:
     * 1. 二分答案法：半径范围在[0, maxPosition - minPosition]之间
     * 2. 对于每个候选半径mid，检查是否能通过点燃一个点引爆所有点
     * 3. 贪心验证：从最左边开始，每次尽可能远地放置引爆点
     * 
     * 时间复杂度: O(n log(maxDistance))
     * 空间复杂度: O(1)
     * 是否最优解: 是
     * 
     * @param haybales 干草堆的位置数组
     * @return 最小爆炸半径
     */
    public static int angryCows(int[] haybales) {
        Arrays.sort(haybales);
        int left = 0, right = haybales[haybales.length - 1] - haybales[0];
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (canExplodeAll(haybales, mid)) right = mid;
            else left = mid + 1;
        }
        return left;
    }
    
    private static boolean canExplodeAll(int[] haybales, int radius) {
        int n = haybales.length;
        int count = 1;
        int lastPos = haybales[0];
        for (int i = 1; i < n; i++) {
            if (haybales[i] - lastPos > 2 * radius) {
                count++;
                lastPos = haybales[i];
            }
        }
        return count <= 1; // 只需要一个引爆点就能引爆所有干草堆
    }
    
    /**
     * HackerRank: 最小化最大值
     * 题目来源: https://www.hackerrank.com/challenges/min-max/problem
     * 
     * 题目描述:
     * 将一个数组分割成K个非空的连续子数组，设计一个算法使得这K个子数组各自和的最大值最小。
     * 
     * 思路分析:
     * 1. 二分答案法：答案范围在[max(nums), sum(nums)]之间
     * 2. 对于每个可能的答案mid，检查能否将数组分成<=K个子数组且每个子数组和<=mid
     * 3. 如果可以，说明答案可能更小，向左搜索；否则向右搜索
     * 
     * 时间复杂度: O(n log(sum))
     * 空间复杂度: O(1)
     * 是否最优解: 是
     * 
     * @param nums 数组
     * @param K 分割的子数组数量
     * @return 最小的最大子数组和
     */
    public static int minimizeMaximum(int[] nums, int K) {
        int left = 0, right = 0;
        for (int num : nums) {
            left = Math.max(left, num);
            right += num;
        }
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (canSplitIntoK(nums, K, mid)) right = mid;
            else left = mid + 1;
        }
        return left;
    }
    
    private static boolean canSplitIntoK(int[] nums, int K, int maxSum) {
        int count = 1, currentSum = 0;
        for (int num : nums) {
            if (currentSum + num > maxSum) {
                count++;
                currentSum = num;
                if (count > K) return false;
            } else currentSum += num;
        }
        return true;
    }
    
    /**
     * 计蒜客: 跳石头
     * 题目来源: https://nanti.jisuanke.com/t/T1201
     * 
     * 题目描述:
     * 河道中分布着一些巨大岩石，组委会计划移走一些岩石，使得选手们在比赛过程中的最短跳跃距离尽可能长。
     * 
     * 思路分析:
     * 1. 二分答案法：距离范围在[0, L]之间
     * 2. 对于每个候选距离mid，计算需要移走多少块岩石
     * 3. 如果需要移走的岩石数不超过M，则可以尝试更大的距离
     * 
     * 时间复杂度: O(n log L)
     * 空间复杂度: O(1)
     * 是否最优解: 是
     * 
     * @param rocks 岩石位置数组
     * @param M 最多可移走的岩石数
     * @param L 河道长度
     * @return 最大的最短跳跃距离
     */
    public static int jumpStones(int[] rocks, int M, int L) {
        Arrays.sort(rocks);
        int left = 0, right = L, result = 0;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (canJump(rocks, M, mid, L)) {
                result = mid;
                left = mid + 1;
            } else right = mid - 1;
        }
        return result;
    }
    
    private static boolean canJump(int[] rocks, int M, int minDist, int L) {
        int count = 0, lastPos = 0;
        for (int rock : rocks) {
            if (rock - lastPos < minDist) count++;
            else lastPos = rock;
        }
        if (L - lastPos < minDist) count++;
        return count <= M;
    }
    
    /**
     * Codeforces 474B - Worms
     * 题目来源: https://codeforces.com/problemset/problem/474/B
     * 
     * 题目描述:
     * 有n堆虫子，第i堆有a_i条虫子。每条虫子都有一个编号，从1开始连续编号。
     * 给出m个查询，每个查询给出一个编号x，问编号为x的虫子属于哪一堆。
     * 
     * 思路分析:
     * 1. 使用前缀和数组记录每堆虫子的结束位置
     * 2. 对于每个查询，使用二分查找在前缀和数组中查找x所在的位置
     * 3. 找到第一个前缀和大于等于x的位置
     * 
     * 时间复杂度: O(n + m log n)
     * 空间复杂度: O(n)
     * 是否最优解: 是
     * 
     * @param piles 每堆虫子的数量数组
     * @param queries 查询数组
     * @return 每个查询对应的堆编号
     */
    public static int[] findWormPiles(int[] piles, int[] queries) {
        int n = piles.length;
        int m = queries.length;
        
        // 计算前缀和
        long[] prefixSum = new long[n];
        prefixSum[0] = piles[0];
        for (int i = 1; i < n; i++) {
            prefixSum[i] = prefixSum[i - 1] + piles[i];
        }
        
        int[] result = new int[m];
        
        for (int i = 0; i < m; i++) {
            long x = queries[i];
            
            // 使用二分查找找到第一个前缀和 >= x 的位置
            int left = 0, right = n - 1;
            int pileIndex = -1;
            
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                
                if (prefixSum[mid] >= x) {
                    pileIndex = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            
            result[i] = pileIndex + 1;  // 1-based索引
        }
        
        return result;
    }
    
    /**
     * SPOJ - AGGRCOW (Aggressive cows)
     * 题目来源: https://www.spoj.com/problems/AGGRCOW/
     * 
     * 题目描述:
     * 农夫约翰建造了一个有 C 个摊位的畜棚，摊位位于 x0, ..., x(C-1)。
     * 他的 M 头奶牛总是相互攻击，约翰必须以某种方式分配奶牛到摊位，使它们之间的最小距离尽可能大。
     * 
     * 思路分析:
     * 1. 二分答案法：距离范围在[0, maxPosition - minPosition]之间
     * 2. 对于每个候选距离mid，使用贪心算法验证是否可以放置所有奶牛
     * 3. 如果可以，说明可以尝试更大的距离；否则需要减小距离
     * 
     * 时间复杂度: O(n log(max-min) * n)
     * 空间复杂度: O(1)
     * 是否最优解: 是
     */
    
    // 测试方法
    public static void main(String[] args) {
        // 直接运行所有测试用例
        
        // 测试基础二分查找
        int[] nums1 = {-1, 0, 3, 5, 9, 12};
        System.out.println("基础二分查找测试:");
        System.out.println("查找9: " + search(nums1, 9)); // 应该输出4
        System.out.println("查找2: " + search(nums1, 2)); // 应该输出-1
        
        // 测试查找插入位置
        int[] nums2 = {1, 3, 5, 6};
        System.out.println("\n查找插入位置测试:");
        System.out.println("查找5: " + searchInsert(nums2, 5)); // 应该输出2
        System.out.println("查找2: " + searchInsert(nums2, 2)); // 应该输出1
        System.out.println("查找7: " + searchInsert(nums2, 7)); // 应该输出4
        System.out.println("查找0: " + searchInsert(nums2, 0)); // 应该输出0
        
        // 测试查找范围
        int[] nums3 = {5, 7, 7, 8, 8, 10};
        System.out.println("\n查找范围测试:");
        int[] range = searchRange(nums3, 8);
        System.out.println("查找8: [" + range[0] + ", " + range[1] + "]"); // 应该输出[3, 4]
        
        range = searchRange(nums3, 6);
        System.out.println("查找6: [" + range[0] + ", " + range[1] + "]"); // 应该输出[-1, -1]
        
        // 测试完全平方数
        System.out.println("\n完全平方数测试:");
        System.out.println("16是完全平方数: " + isPerfectSquare(16)); // 应该输出true
        System.out.println("14是完全平方数: " + isPerfectSquare(14)); // 应该输出false
        
        // 测试寻找峰值
        int[] nums4 = {1, 2, 3, 1};
        System.out.println("\n寻找峰值测试:");
        System.out.println("峰值索引: " + findPeakElement(nums4)); // 应该输出2
        
        // 测试寻找旋转数组最小值
        int[] nums5 = {3, 4, 5, 1, 2};
        System.out.println("\n寻找旋转数组最小值测试:");
        System.out.println("最小值: " + findMin(nums5)); // 应该输出1
        
        // 测试搜索二维矩阵
        int[][] matrix = {{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
        System.out.println("\n搜索二维矩阵测试:");
        System.out.println("查找3: " + searchMatrix(matrix, 3)); // 应该输出true
        System.out.println("查找13: " + searchMatrix(matrix, 13)); // 应该输出false
        
        // 测试搜索旋转排序数组
        int[] nums6 = {4, 5, 6, 7, 0, 1, 2};
        System.out.println("\n搜索旋转排序数组测试:");
        System.out.println("查找0: " + searchInRotatedSortedArray(nums6, 0)); // 应该输出4
        System.out.println("查找3: " + searchInRotatedSortedArray(nums6, 3)); // 应该输出-1
        
        // 测试寻找重复数
        int[] nums7 = {1, 3, 4, 2, 2};
        System.out.println("\n寻找重复数测试:");
        System.out.println("重复数: " + findDuplicate(nums7)); // 应该输出2
        
        // 测试Codeforces题目
        int[] prices = {1, 2, 3, 4, 5};
        int[] queries = {3, 10, 1};
        int[] drinks = howManyDrinks(prices, queries);
        System.out.println("\nCodeforces题目测试:");
        System.out.println("可以购买的饮料种类数: " + Arrays.toString(drinks)); // 应该输出[3, 5, 1]
        
        // 测试SPOJ题目
        int[] positions = {1, 2, 8, 4, 9};
        System.out.println("\nSPOJ题目测试:");
        System.out.println("最大最小距离: " + aggressiveCows(positions, 3)); // 应该输出3
        
        // 测试LeetCode 69. Sqrt(x)
        System.out.println("\nLeetCode 69. Sqrt(x)测试:");
        System.out.println("Sqrt(4): " + mySqrt(4)); // 应该输出 2
        System.out.println("Sqrt(8): " + mySqrt(8)); // 应该输出 2
        System.out.println("Sqrt(0): " + mySqrt(0)); // 应该输出 0
        System.out.println("Sqrt(1): " + mySqrt(1)); // 应该输出 1
        System.out.println("Sqrt(2147395599): " + mySqrt(2147395599)); // 大数值测试
        
        // 测试LeetCode 374. 猜数字大小
        System.out.println("\nLeetCode 374. 猜数字大小测试:");
        System.out.println("猜数字(10): " + guessNumber(10)); // 应该输出 6
        System.out.println("猜数字(1): " + guessNumber(1)); // 应该输出 1
        
        // 测试LeetCode 875. 爱吃香蕉的珂珂
        System.out.println("\nLeetCode 875. 爱吃香蕉的珂珂测试:");
        int[] piles = {3, 6, 7, 11};
        System.out.println("最小吃香蕉速度(8小时): " + minEatingSpeed(piles, 8)); // 应该输出 4
        int[] piles2 = {30, 11, 23, 4, 20};
        System.out.println("最小吃香蕉速度(5小时): " + minEatingSpeed(piles2, 5)); // 应该输出 30
        System.out.println("最小吃香蕉速度(6小时): " + minEatingSpeed(piles2, 6)); // 应该输出 23
        
        // 测试LeetCode 1011. 在D天内送达包裹的能力
        System.out.println("\nLeetCode 1011. 在D天内送达包裹的能力测试:");
        int[] weights = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("最小运载能力(5天): " + shipWithinDays(weights, 5)); // 应该输出 15
        int[] weights2 = {3, 2, 2, 4, 1, 4};
        System.out.println("最小运载能力(3天): " + shipWithinDays(weights2, 3)); // 应该输出 6
        int[] weights3 = {1, 2, 3, 1, 1};
        System.out.println("最小运载能力(4天): " + shipWithinDays(weights3, 4)); // 应该输出 3
        
        // 测试AtCoder ABC023D - 射撃王
        System.out.println("\nAtCoder ABC023D - 射撃王测试:");
        int[][] points1 = {{0, 0}, {1, 1}, {2, 2}, {3, 3}}; // 所有点共线，只需要1颗子弹
        System.out.println("最少子弹数: " + minBullets(points1)); // 应该输出 1
        int[][] points2 = {{0, 0}, {1, 0}, {0, 1}, {1, 1}}; // 四个点形成正方形，需要2颗子弹
        System.out.println("最少子弹数: " + minBullets(points2)); // 应该输出 2
        
        // 测试LeetCode 410: 分割数组的最大值
        System.out.println("\nLeetCode 410: 分割数组的最大值测试:");
        int[] splitNums1 = {7, 2, 5, 10, 8};
        System.out.println("最小的最大子数组和(m=2): " + splitArray(splitNums1, 2)); // 应该输出 18
        int[] splitNums2 = {1, 2, 3, 4, 5};
        System.out.println("最小的最大子数组和(m=2): " + splitArray(splitNums2, 2)); // 应该输出 9
        
        // 测试LeetCode 4: 寻找两个正序数组的中位数
        System.out.println("\nLeetCode 4: 寻找两个正序数组的中位数测试:");
        int[] medianNums1 = {1, 3};
        int[] medianNums2 = {2};
        System.out.println("中位数: " + findMedianSortedArrays(medianNums1, medianNums2)); // 应该输出 2.0
        int[] medianNums3 = {1, 2};
        int[] medianNums4 = {3, 4};
        System.out.println("中位数: " + findMedianSortedArrays(medianNums3, medianNums4)); // 应该输出 2.5
        
        // 测试牛客/剑指Offer: 数字在排序数组中出现的次数
        System.out.println("\n牛客/剑指Offer: 数字在排序数组中出现的次数测试:");
        int[] countNums = {1, 2, 3, 3, 3, 3, 4, 5};
        System.out.println("3出现的次数: " + getNumberOfK(countNums, 3)); // 应该输出 4
        System.out.println("6出现的次数: " + getNumberOfK(countNums, 6)); // 应该输出 0
        
        // 测试LeetCode 540: 有序数组中的单一元素
        System.out.println("\nLeetCode 540: 有序数组中的单一元素测试:");
        int[] singleNums1 = {1, 1, 2, 3, 3, 4, 4, 8, 8};
        System.out.println("单一元素: " + singleNonDuplicate(singleNums1)); // 应该输出 2
        int[] singleNums2 = {3, 3, 7, 7, 10, 11, 11};
        System.out.println("单一元素: " + singleNonDuplicate(singleNums2)); // 应该输出 10
        
        // 测试LeetCode 1482: 制作 m 束花所需的最少天数
        System.out.println("\nLeetCode 1482: 制作 m 束花所需的最少天数测试:");
        int[] bloomDays1 = {1, 10, 3, 10, 2};
        System.out.println("最少天数(m=3, k=1): " + minDays(bloomDays1, 3, 1)); // 应该输出 3
        int[] bloomDays2 = {1, 10, 3, 10, 2};
        System.out.println("最少天数(m=3, k=2): " + minDays(bloomDays2, 3, 2)); // 应该输出 -1
        int[] bloomDays3 = {7, 7, 7, 7, 12, 7, 7};
        System.out.println("最少天数(m=2, k=3): " + minDays(bloomDays3, 2, 3)); // 应该输出 12
        
        // 测试新增题目
        System.out.println("\n=== 新增各大平台题目测试 ===");
        
        // 测试牛客网题目
        int[] rotateArray = {3, 4, 5, 1, 2};
        System.out.println("牛客网-旋转数组最小数字: " + minNumberInRotateArray(rotateArray)); // 应该输出1
        
        // 测试acwing题目
        System.out.println("acwing-三次方根(27): " + cubeRoot(27.0)); // 应该输出3.0
        
        // 测试杭电OJ题目
        int[] A = {1, 2, 3}, B = {4, 5, 6}, C = {7, 8, 9};
        System.out.println("杭电OJ-最接近和(15): " + findClosestSum(A, B, C, 15)); // 应该输出true
        
        // 测试POJ题目
        int[] expenses = {100, 200, 300, 400, 500};
        System.out.println("POJ-月度开销(3段): " + monthlyExpense(expenses, 3)); // 应该输出500
        
        // 测试洛谷题目
        int[] trees = {20, 15, 10, 17};
        System.out.println("洛谷-砍树(7米): " + cutTrees(trees, 7)); // 应该输出15
        
        // 测试USACO题目
        int[] haybales = {1, 2, 4, 8, 9};
        System.out.println("USACO-愤怒的奶牛: " + angryCows(haybales)); // 应该输出4
        
        // 测试HackerRank题目
        int[] nums = {1, 2, 3, 4, 5};
        System.out.println("HackerRank-最小化最大值(3段): " + minimizeMaximum(nums, 3)); // 应该输出6
        
        // 测试计蒜客题目
        int[] rocks = {2, 11, 14, 17, 21};
        System.out.println("计蒜客-跳石头(移2块): " + jumpStones(rocks, 2, 25)); // 应该输出4
        
        // 测试Codeforces题目
        int[] wormPiles = {1, 3, 2, 4};
        int[] wormQueries = {1, 4, 7, 10};
        int[] wormResults = findWormPiles(wormPiles, wormQueries);
        System.out.println("Codeforces-虫子堆: " + Arrays.toString(wormResults)); // 应该输出[1, 2, 3, 4]
    }
}