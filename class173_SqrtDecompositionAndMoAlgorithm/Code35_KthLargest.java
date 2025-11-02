// 区间第k大 - 分块算法实现 (Java版本)
// 题目来源: POJ 2104
// 题目链接: http://poj.org/problem?id=2104
// 题目大意: 多次查询区间[l,r]内第k小的数字
// 约束条件: 数组长度n ≤ 1e5，查询次数m ≤ 1e4

import java.io.*;
import java.util.*;

public class Code35_KthLargest {
    static final int MAXN = 100005;
    
    static int n, m, blen; // blen为块的大小
    static int[] arr; // 原始数组
    static int[] sortedArr; // 排序后的数组，用于二分查找
    static List<List<Integer>> blocks; // 每个块排序后的数组
    
    // 初始化分块结构
    static void init() {
        blen = (int)Math.sqrt(n);
        if (blen == 0) blen = 1;
        
        // 复制数组并排序，用于二分查找
        sortedArr = Arrays.copyOf(arr, n);
        Arrays.sort(sortedArr);
        
        // 分块并对每个块进行排序
        int blockCount = (n + blen - 1) / blen;
        blocks = new ArrayList<>(blockCount);
        
        for (int i = 0; i < blockCount; i++) {
            List<Integer> block = new ArrayList<>();
            int start = i * blen;
            int end = Math.min((i + 1) * blen, n);
            
            for (int j = start; j < end; j++) {
                block.add(arr[j]);
            }
            Collections.sort(block);
            blocks.add(block);
        }
    }
    
    // 查询区间[l,r]内小于等于x的元素个数
    static int queryCount(int l, int r, int x) {
        int count = 0;
        int leftBlock = l / blen;
        int rightBlock = r / blen;
        
        if (leftBlock == rightBlock) {
            // 所有元素都在同一个块内
            for (int i = l; i <= r; i++) {
                if (arr[i] <= x) {
                    count++;
                }
            }
        } else {
            // 处理左边不完整的块
            for (int i = l; i < (leftBlock + 1) * blen; i++) {
                if (arr[i] <= x) {
                    count++;
                }
            }
            
            // 处理中间完整的块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                // 利用块的有序性进行二分查找
                List<Integer> block = blocks.get(i);
                // 使用二分查找找到第一个大于x的元素的位置
                int pos = Collections.binarySearch(block, x);
                if (pos >= 0) {
                    // 找到x，需要找到所有等于x的元素
                    while (pos + 1 < block.size() && block.get(pos + 1) == x) {
                        pos++;
                    }
                    count += pos + 1;
                } else {
                    // 没找到x，返回插入点的相反数减1
                    count += -pos - 1;
                }
            }
            
            // 处理右边不完整的块
            for (int i = rightBlock * blen; i <= r; i++) {
                if (arr[i] <= x) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    // 二分查找第k小的元素
    static int findKthSmallest(int l, int r, int k) {
        int left = 0, right = n - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            int x = sortedArr[mid];
            int cnt = queryCount(l, r, x);
            
            if (cnt >= k) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return sortedArr[left];
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        arr = new int[n];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        init();
        
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(st.nextToken()) - 1; // 转换为0-based索引
            int r = Integer.parseInt(st.nextToken()) - 1; // 转换为0-based索引
            int k = Integer.parseInt(st.nextToken());
            
            int result = findKthSmallest(l, r, k);
            pw.println(result);
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
    
    /*
     * 时间复杂度分析：
     * - 初始化：O(n log n)
     * - 单次查询：
     *   - queryCount函数：O(√n + √n log √n) = O(√n log n)
     *   - findKthSmallest函数：O(log n)次queryCount调用
     *   - 总体单次查询时间复杂度：O(√n (log n)^2)
     * - 对于m次查询，总体时间复杂度：O(n log n + m√n (log n)^2)
     * 
     * 空间复杂度分析：
     * - 数组arr：O(n)
     * - 数组sortedArr：O(n)
     * - 块排序数组blocks：O(n)
     * - 总体空间复杂度：O(n)
     * 
     * Java语言特性注意事项：
     * 1. 使用ArrayList存储排序后的块
     * 2. 使用Collections.sort和Collections.binarySearch进行排序和二分查找
     * 3. 注意处理Collections.binarySearch的返回值
     * 4. 输入输出使用BufferedReader和PrintWriter提高效率
     * 
     * 算法说明：
     * 这是一个经典的区间第k小查询问题，分块算法是一种有效的解决方案。
     * 算法结合了二分查找和分块处理的思想，通过统计区间内小于等于候选值的元素个数，
     * 来确定第k小的元素。
     */
}