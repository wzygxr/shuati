/**
 * 排序算法扩展题目 - Java版本
 * 包含更多LeetCode、牛客网、剑指Offer等平台的排序相关题目
 * 每个题目都包含多种解法和详细分析
 * 
 * 时间复杂度分析：详细分析每种解法的时间复杂度
 * 空间复杂度分析：分析内存使用情况
 * 最优解判断：确定是否为最优解，如果不是则寻找最优解
 * 
 * 题目链接汇总:
 * - 88. 合并两个有序数组: https://leetcode.cn/problems/merge-sorted-array/
 * - 973. 最接近原点的K个点: https://leetcode.cn/problems/k-closest-points-to-origin/
 * - 1054. 距离相等的条形码: https://leetcode.cn/problems/distant-barcodes/
 * - 324. 摆动排序 II: https://leetcode.cn/problems/wiggle-sort-ii/
 * - 280. 摆动排序: https://leetcode.cn/problems/wiggle-sort/
 * - 493. 翻转对: https://leetcode.cn/problems/reverse-pairs/
 * - 剑指Offer 45. 把数组排成最小的数
 * - HackerRank - Counting Inversions: https://www.hackerrank.com/challenges/ctci-merge-sort
 * - 牛客网 NC140 排序: https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
 */

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ExtendedSortProblems {
    
    /**
     * 题目1: 88. 合并两个有序数组
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/merge-sorted-array/
     * 难度: 简单
     * 
     * 题目描述:
     * 给你两个按非递减顺序排列的整数数组 nums1 和 nums2，另有两个整数 m 和 n，
     * 分别表示 nums1 和 nums2 中的元素数目。请你合并 nums2 到 nums1 中，使合并后的数组同样按非递减顺序排列。
     * 
     * 示例:
     * 输入: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
     * 输出: [1,2,2,3,5,6]
     * 
     * 解法分析:
     * 方法1: 双指针从后向前合并 (最优解)
     * 方法2: 先合并后排序
     * 
     * 时间复杂度: O(m + n) - 方法1
     * 空间复杂度: O(1) - 方法1
     * 是否最优解: 是
     */
    public static void mergeSortedArrays(int[] nums1, int m, int[] nums2, int n) {
        if (nums1 == null || nums2 == null || m < 0 || n < 0) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        
        int p1 = m - 1; // nums1有效部分的末尾
        int p2 = n - 1; // nums2的末尾
        int p = m + n - 1; // 合并后的末尾
        
        // 从后向前合并，避免覆盖nums1中的元素
        while (p1 >= 0 && p2 >= 0) {
            if (nums1[p1] > nums2[p2]) {
                nums1[p] = nums1[p1];
                p1--;
            } else {
                nums1[p] = nums2[p2];
                p2--;
            }
            p--;
        }
        
        // 如果nums2还有剩余元素，直接复制到nums1前面
        while (p2 >= 0) {
            nums1[p] = nums2[p2];
            p2--;
            p--;
        }
    }
    
    /**
     * 题目2: 973. 最接近原点的K个点
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/k-closest-points-to-origin/
     * 难度: 中等
     * 
     * 题目描述:
     * 给定一个数组 points，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点，
     * 返回离原点 (0,0) 最近的 k 个点。
     * 
     * 示例:
     * 输入: points = [[1,3],[-2,2]], k = 1
     * 输出: [[-2,2]]
     * 
     * 解法分析:
     * 方法1: 快速选择算法 (最优解)
     * 方法2: 最大堆维护K个最小距离
     * 方法3: 排序后取前K个
     * 
     * 时间复杂度: O(n) 平均 - 方法1
     * 空间复杂度: O(1) - 方法1
     * 是否最优解: 是
     */
    public static int[][] kClosest(int[][] points, int k) {
        if (points == null || points.length == 0 || k <= 0 || k > points.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        
        // 使用快速选择算法找到第k小的距离
        quickSelect(points, 0, points.length - 1, k);
        
        // 返回前k个点
        return Arrays.copyOf(points, k);
    }
    
    private static void quickSelect(int[][] points, int left, int right, int k) {
        if (left >= right) return;
        
        // 随机选择pivot
        int pivotIndex = ThreadLocalRandom.current().nextInt(left, right + 1);
        int pivotDist = distance(points[pivotIndex]);
        
        // 分区操作
        int i = left;
        for (int j = left; j <= right; j++) {
            if (distance(points[j]) <= pivotDist) {
                swap(points, i, j);
                i++;
            }
        }
        
        // 根据分区结果决定下一步
        if (i == k) {
            return;
        } else if (i < k) {
            quickSelect(points, i, right, k);
        } else {
            quickSelect(points, left, i - 1, k);
        }
    }
    
    private static int distance(int[] point) {
        return point[0] * point[0] + point[1] * point[1];
    }
    
    private static void swap(int[][] points, int i, int j) {
        int[] temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }
    
    /**
     * 题目3: 1054. 距离相等的条形码
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/distant-barcodes/
     * 难度: 中等
     * 
     * 题目描述:
     * 在一个仓库里，有一排条形码，其中第 i 个条形码为 barcodes[i]。
     * 请你重新排列这些条形码，使其中任意两个相邻的条形码不能相等。
     * 
     * 示例:
     * 输入: barcodes = [1,1,1,2,2,2]
     * 输出: [2,1,2,1,2,1]
     * 
     * 解法分析:
     * 方法1: 最大堆按频率排序 (最优解)
     * 方法2: 计数排序+间隔填充
     * 
     * 时间复杂度: O(n log k) - 方法1，k为不同条形码的数量
     * 空间复杂度: O(n) - 方法1
     * 是否最优解: 是
     */
    public static int[] rearrangeBarcodes(int[] barcodes) {
        if (barcodes == null || barcodes.length == 0) {
            return new int[0];
        }
        
        // 统计频率
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int code : barcodes) {
            freqMap.put(code, freqMap.getOrDefault(code, 0) + 1);
        }
        
        // 最大堆，按频率排序
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            maxHeap.offer(new int[]{entry.getKey(), entry.getValue()});
        }
        
        int[] result = new int[barcodes.length];
        int index = 0;
        
        // 间隔填充，先填偶数位置，再填奇数位置
        while (!maxHeap.isEmpty()) {
            int[] current = maxHeap.poll();
            int code = current[0];
            int freq = current[1];
            
            // 填充所有当前条形码
            for (int i = 0; i < freq; i++) {
                if (index >= result.length) {
                    index = 1; // 切换到奇数位置
                }
                result[index] = code;
                index += 2;
            }
        }
        
        return result;
    }
    
    /**
     * 题目4: 324. 摆动排序 II
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/wiggle-sort-ii/
     * 难度: 中等
     * 
     * 题目描述:
     * 给你一个整数数组 nums，将它重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的顺序。
     * 
     * 示例:
     * 输入: nums = [1,5,1,1,6,4]
     * 输出: [1,6,1,5,1,4]
     * 
     * 解法分析:
     * 方法1: 排序+双指针 (最优解)
     * 方法2: 快速选择找到中位数+三路划分
     * 
     * 时间复杂度: O(n log n) - 方法1
     * 空间复杂度: O(n) - 方法1
     * 是否最优解: 是（对于通用情况）
     */
    public static void wiggleSort(int[] nums) {
        if (nums == null || nums.length <= 1) return;
        
        // 复制并排序数组
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        
        int n = nums.length;
        int mid = (n + 1) / 2; // 中间位置（向上取整）
        
        // 双指针填充：左半部分从大到小，右半部分从大到小
        int left = mid - 1;
        int right = n - 1;
        int index = 0;
        
        while (index < n) {
            if (index % 2 == 0) {
                // 偶数位置：取左半部分（较小的数）
                nums[index] = sorted[left];
                left--;
            } else {
                // 奇数位置：取右半部分（较大的数）
                nums[index] = sorted[right];
                right--;
            }
            index++;
        }
    }
    
    /**
     * 题目5: 280. 摆动排序
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/wiggle-sort/
     * 难度: 中等
     * 
     * 题目描述:
     * 给你一个无序的数组 nums，将它重新排列成 nums[0] <= nums[1] >= nums[2] <= nums[3]... 的顺序。
     * 
     * 示例:
     * 输入: nums = [3,5,2,1,6,4]
     * 输出: [1,6,2,5,3,4] 或类似
     * 
     * 解法分析:
     * 方法1: 一次遍历交换相邻元素 (最优解)
     * 方法2: 排序后间隔交换
     * 
     * 时间复杂度: O(n) - 方法1
     * 空间复杂度: O(1) - 方法1
     * 是否最优解: 是
     */
    public static void wiggleSort280(int[] nums) {
        if (nums == null || nums.length <= 1) return;
        
        // 一次遍历，根据需要交换相邻元素
        for (int i = 0; i < nums.length - 1; i++) {
            if ((i % 2 == 0 && nums[i] > nums[i + 1]) || 
                (i % 2 == 1 && nums[i] < nums[i + 1])) {
                // 交换相邻元素
                int temp = nums[i];
                nums[i] = nums[i + 1];
                nums[i + 1] = temp;
            }
        }
    }
    
    /**
     * 题目6: 493. 翻转对
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/reverse-pairs/
     * 难度: 困难
     * 
     * 题目描述:
     * 给定一个数组 nums，如果 i < j 且 nums[i] > 2 * nums[j]，则称这是一个翻转对。
     * 返回数组中翻转对的数量。
     * 
     * 示例:
     * 输入: nums = [1,3,2,3,1]
     * 输出: 2
     * 
     * 解法分析:
     * 方法1: 归并排序统计翻转对 (最优解)
     * 方法2: 树状数组/线段树
     * 
     * 时间复杂度: O(n log n) - 方法1
     * 空间复杂度: O(n) - 方法1
     * 是否最优解: 是
     */
    public static int reversePairs493(int[] nums) {
        if (nums == null || nums.length <= 1) return 0;
        
        int[] temp = new int[nums.length];
        return mergeSortCountPairs(nums, 0, nums.length - 1, temp);
    }
    
    private static int mergeSortCountPairs(int[] nums, int left, int right, int[] temp) {
        if (left >= right) return 0;
        
        int mid = left + (right - left) / 2;
        int count = 0;
        
        count += mergeSortCountPairs(nums, left, mid, temp);
        count += mergeSortCountPairs(nums, mid + 1, right, temp);
        count += countPairs(nums, left, mid, right);
        merge(nums, left, mid, right, temp);
        
        return count;
    }
    
    private static int countPairs(int[] nums, int left, int mid, int right) {
        int count = 0;
        int j = mid + 1;
        
        // 统计满足 nums[i] > 2 * nums[j] 的对数
        for (int i = left; i <= mid; i++) {
            while (j <= right && (long)nums[i] > 2L * nums[j]) {
                j++;
            }
            count += (j - (mid + 1));
        }
        
        return count;
    }
    
    private static void merge(int[] nums, int left, int mid, int right, int[] temp) {
        for (int i = left; i <= right; i++) {
            temp[i] = nums[i];
        }
        
        int i = left, k = left, j = mid + 1;
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                nums[k++] = temp[i++];
            } else {
                nums[k++] = temp[j++];
            }
        }
        
        while (i <= mid) {
            nums[k++] = temp[i++];
        }
        while (j <= right) {
            nums[k++] = temp[j++];
        }
    }
    
    /**
     * 题目7: 剑指Offer 45. 把数组排成最小的数
     * 来源: 剑指Offer
     * 难度: 中等
     * 
     * 题目描述:
     * 输入一个非负整数数组，把数组里所有数字拼接起来排成一个数，打印能拼接出的所有数字中最小的一个。
     * 
     * 示例:
     * 输入: [10,2]
     * 输出: "102"
     * 
     * 解法分析:
     * 方法1: 自定义排序 (最优解)
     * 方法2: 全排列（不推荐，复杂度高）
     * 
     * 时间复杂度: O(n log n) - 方法1
     * 空间复杂度: O(n) - 方法1
     * 是否最优解: 是
     */
    public static String minNumber(int[] nums) {
        if (nums == null || nums.length == 0) return "";
        
        // 将数字转换为字符串
        String[] strNums = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            strNums[i] = String.valueOf(nums[i]);
        }
        
        // 自定义排序：比较 s1+s2 和 s2+s1
        Arrays.sort(strNums, (s1, s2) -> (s1 + s2).compareTo(s2 + s1));
        
        // 拼接结果
        StringBuilder result = new StringBuilder();
        for (String str : strNums) {
            result.append(str);
        }
        
        return result.toString();
    }
    
    /**
     * 题目8: HackerRank - Counting Inversions
     * 来源: HackerRank
     * 链接: https://www.hackerrank.com/challenges/ctci-merge-sort
     * 难度: 困难
     * 
     * 题目描述:
     * 计算数组中逆序对的数量（扩展版本，处理大数据量）
     * 
     * 解法分析:
     * 方法1: 归并排序统计逆序对 (最优解)
     * 方法2: 树状数组/线段树
     * 
     * 时间复杂度: O(n log n) - 方法1
     * 空间复杂度: O(n) - 方法1
     * 是否最优解: 是
     */
    public static long countInversions(int[] arr) {
        if (arr == null || arr.length <= 1) return 0;
        
        int[] temp = new int[arr.length];
        return mergeSortCountInversions(arr, 0, arr.length - 1, temp);
    }
    
    private static long mergeSortCountInversions(int[] arr, int left, int right, int[] temp) {
        if (left >= right) return 0;
        
        int mid = left + (right - left) / 2;
        long count = 0;
        
        count += mergeSortCountInversions(arr, left, mid, temp);
        count += mergeSortCountInversions(arr, mid + 1, right, temp);
        count += mergeAndCount(arr, left, mid, right, temp);
        
        return count;
    }
    
    private static long mergeAndCount(int[] arr, int left, int mid, int right, int[] temp) {
        for (int i = left; i <= right; i++) {
            temp[i] = arr[i];
        }
        
        int i = left, j = mid + 1, k = left;
        long count = 0;
        
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
                count += (mid - i + 1); // 统计逆序对
            }
        }
        
        while (i <= mid) {
            arr[k++] = temp[i++];
        }
        while (j <= right) {
            arr[k++] = temp[j++];
        }
        
        return count;
    }
    
    /**
     * 题目9: 牛客网 NC140 排序
     * 来源: 牛客网
     * 链接: https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
     * 难度: 简单
     * 
     * 题目描述:
     * 实现各种排序算法对数组进行排序
     * 
     * 解法分析:
     * 根据数据规模选择合适算法
     * - 小数据: 插入排序
     * - 中等数据: 快速排序
     * - 大数据: 归并排序
     * - 需要稳定: 归并排序
     * 
     * 时间复杂度: 根据算法选择
     * 空间复杂度: 根据算法选择
     */
    public static int[] sortArrayNC140(int[] arr) {
        if (arr == null || arr.length <= 1) return arr;
        
        // 根据数据规模选择算法
        if (arr.length < 50) {
            // 小数组使用插入排序
            insertionSort(arr);
        } else {
            // 中等以上使用快速排序
            quickSort(arr, 0, arr.length - 1);
        }
        
        return arr;
    }
    
    private static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
    
    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }
    
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        
        swap(arr, i + 1, high);
        return i + 1;
    }
    
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    /**
     * 题目10: 外部排序模拟 - 多路归并
     * 来源: 算法导论
     * 难度: 困难
     * 
     * 题目描述:
     * 模拟外部排序的多路归并过程，处理无法一次性加载到内存的大数据
     * 
     * 解法分析:
     * 方法1: 多路归并排序
     * 方法2: 败者树优化
     * 
     * 时间复杂度: O(n log k) - k为归并路数
     * 空间复杂度: O(k) - 缓冲区大小
     */
    public static class ExternalSort {
        
        /**
         * 模拟多路归并排序
         * @param chunks 多个有序数据块
         * @return 合并后的有序数组
         */
        public static int[] multiwayMerge(List<int[]> chunks) {
            if (chunks == null || chunks.isEmpty()) return new int[0];
            
            // 使用优先队列进行多路归并
            PriorityQueue<Element> minHeap = new PriorityQueue<>();
            
            // 初始化每个数据块的指针
            int[] pointers = new int[chunks.size()];
            int totalSize = 0;
            
            // 将每个数据块的第一个元素加入堆
            for (int i = 0; i < chunks.size(); i++) {
                int[] chunk = chunks.get(i);
                totalSize += chunk.length;
                if (chunk.length > 0) {
                    minHeap.offer(new Element(chunk[0], i, 0));
                    pointers[i] = 1;
                }
            }
            
            int[] result = new int[totalSize];
            int index = 0;
            
            // 多路归并
            while (!minHeap.isEmpty()) {
                Element min = minHeap.poll();
                result[index++] = min.value;
                
                int chunkIndex = min.chunkIndex;
                int elementIndex = min.elementIndex + 1;
                int[] chunk = chunks.get(chunkIndex);
                
                if (elementIndex < chunk.length) {
                    minHeap.offer(new Element(chunk[elementIndex], chunkIndex, elementIndex));
                }
            }
            
            return result;
        }
        
        static class Element implements Comparable<Element> {
            int value;
            int chunkIndex;
            int elementIndex;
            
            Element(int value, int chunkIndex, int elementIndex) {
                this.value = value;
                this.chunkIndex = chunkIndex;
                this.elementIndex = elementIndex;
            }
            
            @Override
            public int compareTo(Element other) {
                return Integer.compare(this.value, other.value);
            }
        }
    }
    
    /**
     * 测试所有扩展题目
     */
    public static void testAllProblems() {
        System.out.println("=== 扩展排序题目测试开始 ===");
        
        // 测试题目1: 合并两个有序数组
        System.out.println("\n题目1: 合并两个有序数组");
        int[] nums1 = new int[]{1,2,3,0,0,0};
        int[] nums2 = new int[]{2,5,6};
        mergeSortedArrays(nums1, 3, nums2, 3);
        System.out.println("合并结果: " + Arrays.toString(nums1));
        
        // 测试题目2: 最接近原点的K个点
        System.out.println("\n题目2: 最接近原点的K个点");
        int[][] points = new int[][]{{1,3}, {-2,2}, {5,8}, {0,1}};
        int[][] result2 = kClosest(points, 2);
        System.out.println("最接近的2个点: " + Arrays.deepToString(result2));
        
        // 测试题目3: 距离相等的条形码
        System.out.println("\n题目3: 距离相等的条形码");
        int[] barcodes = new int[]{1,1,1,2,2,2};
        int[] result3 = rearrangeBarcodes(barcodes);
        System.out.println("重新排列结果: " + Arrays.toString(result3));
        
        // 测试题目4: 摆动排序II
        System.out.println("\n题目4: 摆动排序II");
        int[] nums4 = new int[]{1,5,1,1,6,4};
        wiggleSort(nums4);
        System.out.println("摆动排序结果: " + Arrays.toString(nums4));
        
        // 测试题目5: 摆动排序
        System.out.println("\n题目5: 摆动排序");
        int[] nums5 = new int[]{3,5,2,1,6,4};
        wiggleSort280(nums5);
        System.out.println("摆动排序结果: " + Arrays.toString(nums5));
        
        // 测试题目6: 翻转对
        System.out.println("\n题目6: 翻转对");
        int[] nums6 = new int[]{1,3,2,3,1};
        int count6 = reversePairs493(nums6);
        System.out.println("翻转对数量: " + count6);
        
        // 测试题目7: 把数组排成最小的数
        System.out.println("\n题目7: 把数组排成最小的数");
        int[] nums7 = new int[]{10,2};
        String result7 = minNumber(nums7);
        System.out.println("最小数字: " + result7);
        
        // 测试题目8: 逆序对计数
        System.out.println("\n题目8: 逆序对计数");
        int[] nums8 = new int[]{2,4,1,3,5};
        long count8 = countInversions(nums8);
        System.out.println("逆序对数量: " + count8);
        
        // 测试题目9: 牛客网排序
        System.out.println("\n题目9: 牛客网排序");
        int[] nums9 = new int[]{3,1,4,1,5,9,2,6};
        int[] result9 = sortArrayNC140(nums9);
        System.out.println("排序结果: " + Arrays.toString(result9));
        
        // 测试题目10: 外部排序
        System.out.println("\n题目10: 外部排序模拟");
        List<int[]> chunks = Arrays.asList(
            new int[]{1,3,5},
            new int[]{2,4,6},
            new int[]{0,7,8}
        );
        int[] result10 = ExternalSort.multiwayMerge(chunks);
        System.out.println("多路归并结果: " + Arrays.toString(result10));
        
        System.out.println("\n=== 扩展排序题目测试结束 ===");
    }
    
    public static void main(String[] args) {
        try {
            testAllProblems();
        } catch (Exception e) {
            System.err.println("测试过程中出现错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}