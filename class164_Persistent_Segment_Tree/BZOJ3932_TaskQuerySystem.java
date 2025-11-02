package class157;

import java.io.*;
import java.util.*;

/**
 * BZOJ 3932 [CQOI2015]任务查询系统
 * 
 * 题目来源: BZOJ 3932
 * 题目链接: https://www.lydsy.com/JudgeOnline/problem.php?id=3932
 * 
 * 题目描述:
 * 最近实验室正在为其管理的超级计算机编制一套任务管理系统，而你被安排完成其中的查询部分。
 * 超级计算机中的任务用三元组(Si,Ei,Pi)描述，(Si,Ei,Pi)表示任务从第Si秒开始，在第Ei秒后结束（第Ei秒结束），
 * 其优先级为Pi。同一时间可能有多个任务同时执行，它们的优先级可能相同，也可能不同。
 * 调度系统会经常向查询系统询问，第Xi秒正在执行的任务中，优先级第Yi小的任务的优先级是多少。
 * 在任意两个时刻，不会有相同优先级的任务正在执行。
 * 
 * 解题思路:
 * 使用可持久化线段树解决任务查询问题。
 * 1. 将所有任务按照时间轴进行差分处理，每个任务在开始时间+1，在结束时间+1处-1
 * 2. 按照时间顺序建立可持久化线段树，每个时间点对应一个版本
 * 3. 对于每个查询，在对应时间点的线段树版本中查询第K小的优先级
 * 
 * 时间复杂度: O((n+m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 1 <= n, m <= 10^5
 * 1 <= Si, Ei <= 10^9
 * 1 <= Pi <= 10^9
 * 1 <= Xi <= 10^9
 * 1 <= Yi <= sum(Pj) (第Xj秒正在运行的任务总数)
 * 
 * 示例:
 * 输入:
 * 2 3
 * 1 2 6
 * 2 3 3
 * 1 1
 * 2 1
 * 3 1
 * 
 * 输出:
 * 6
 * 3
 * 3
 */
public class BZOJ3932_TaskQuerySystem {

    public static int MAXN = 100010;
    
    // 任务信息
    public static int[] S = new int[MAXN];
    public static int[] E = new int[MAXN];
    public static int[] P = new int[MAXN];
    
    // 离散化相关
    public static int[] times = new int[MAXN * 2];
    public static int[] priorities = new int[MAXN];
    
    // 可持久化线段树
    public static int[] root = new int[MAXN * 2];
    public static int[] left = new int[MAXN * 20];
    public static int[] right = new int[MAXN * 20];
    public static int[] sum = new int[MAXN * 20];
    public static int cnt = 0;
    
    // 事件列表 (时间, 优先级, 类型:+1/-1)
    public static List<Event> events = new ArrayList<>();
    
    static class Event {
        int time, priority, type;
        
        Event(int time, int priority, int type) {
            this.time = time;
            this.priority = priority;
            this.type = type;
        }
    }
    
    /**
     * 离散化处理
     */
    public static void discretize(int n) {
        // 收集所有时间点
        int timeIdx = 0;
        for (int i = 1; i <= n; i++) {
            times[timeIdx++] = S[i];
            times[timeIdx++] = E[i] + 1;
        }
        
        // 排序去重
        Arrays.sort(times, 0, timeIdx);
        int uniqueTimeCount = 1;
        for (int i = 1; i < timeIdx; i++) {
            if (times[i] != times[i-1]) {
                times[uniqueTimeCount++] = times[i];
            }
        }
        
        // 收集所有优先级
        for (int i = 1; i <= n; i++) {
            priorities[i-1] = P[i];
        }
        Arrays.sort(priorities, 0, n);
        int uniquePriorityCount = 1;
        for (int i = 1; i < n; i++) {
            if (priorities[i] != priorities[i-1]) {
                priorities[uniquePriorityCount++] = priorities[i];
            }
        }
    }
    
    /**
     * 构建空线段树
     */
    public static int build(int l, int r) {
        int rt = ++cnt;
        sum[rt] = 0;
        if (l < r) {
            int mid = (l + r) / 2;
            left[rt] = build(l, mid);
            right[rt] = build(mid + 1, r);
        }
        return rt;
    }
    
    /**
     * 插入操作
     */
    public static int insert(int pos, int l, int r, int pre, int val) {
        int rt = ++cnt;
        left[rt] = left[pre];
        right[rt] = right[pre];
        sum[rt] = sum[pre] + val;
        
        if (l < r) {
            int mid = (l + r) / 2;
            if (pos <= mid) {
                left[rt] = insert(pos, l, mid, left[rt], val);
            } else {
                right[rt] = insert(pos, mid + 1, r, right[rt], val);
            }
        }
        return rt;
    }
    
    /**
     * 查询第k小
     */
    public static int queryKth(int k, int l, int r, int u, int v) {
        if (l >= r) return l;
        int mid = (l + r) / 2;
        int x = sum[left[v]] - sum[left[u]];
        if (x >= k) {
            return queryKth(k, l, mid, left[u], left[v]);
        } else {
            return queryKth(k - x, mid + 1, r, right[u], right[v]);
        }
    }
    
    /**
     * 二分查找离散化后的索引
     */
    public static int binarySearch(int[] arr, int len, int target) {
        int left = 0, right = len - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = in.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int m = Integer.parseInt(line[1]);
        
        // 读取任务信息
        for (int i = 1; i <= n; i++) {
            line = in.readLine().split(" ");
            S[i] = Integer.parseInt(line[0]);
            E[i] = Integer.parseInt(line[1]);
            P[i] = Integer.parseInt(line[2]);
        }
        
        // 离散化处理
        discretize(n);
        
        // 构建事件列表
        for (int i = 1; i <= n; i++) {
            int startTimeIdx = binarySearch(times, times.length, S[i]);
            int endTimeIdx = binarySearch(times, times.length, E[i] + 1);
            int priorityIdx = binarySearch(priorities, priorities.length, P[i]) + 1;
            
            events.add(new Event(startTimeIdx, priorityIdx, 1));
            events.add(new Event(endTimeIdx, priorityIdx, -1));
        }
        
        // 按时间排序事件
        Collections.sort(events, (a, b) -> {
            if (a.time != b.time) return a.time - b.time;
            return a.type - b.type;
        });
        
        // 构建初始线段树
        root[0] = build(1, n);
        
        // 处理事件，构建可持久化线段树
        int version = 0;
        for (Event event : events) {
            while (version < event.time) {
                root[version + 1] = root[version];
                version++;
            }
            root[version] = insert(event.priority, 1, n, root[version], event.type);
        }
        
        // 处理查询
        long lastAns = 1;
        for (int i = 1; i <= m; i++) {
            line = in.readLine().split(" ");
            int x = Integer.parseInt(line[0]);
            int y = Integer.parseInt(line[1]);
            
            // 根据题目要求调整查询参数
            int k = (int)((lastAns + y) % n + 1);
            
            // 找到对应时间点的版本
            int versionIdx = binarySearch(times, times.length, x);
            if (versionIdx == -1) {
                // 找到小于等于x的最大时间点
                versionIdx = 0;
                for (int j = 0; j < times.length && times[j] <= x; j++) {
                    versionIdx = j;
                }
            }
            
            // 查询第k小的优先级
            if (sum[root[versionIdx]] < k) {
                lastAns = priorities[n-1];
            } else {
                int pos = queryKth(k, 1, n, 0, root[versionIdx]);
                lastAns = priorities[pos-1];
            }
            out.println(lastAns);
        }
        
        out.flush();
        out.close();
        in.close();
    }
}