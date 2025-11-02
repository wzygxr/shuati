package class138;

// 最大密度子图
// 给定一个无向图，找到一个子图使得其密度最大
// 密度定义为子图中边数除以点数
// 1 <= n <= 1000
// 0 <= m <= 10000
// 测试链接 : https://www.luogu.com.cn/problem/UVA1389

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code09_MaximumDensitySubgraph {

    public static int MAXN = 1001;
    
    public static int MAXM = 10001;
    
    // 最小精度
    public static double sml = 1e-9;
    
    // 链式前向星
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXM * 2]; // 无向图，边数翻倍
    public static int[] to = new int[MAXM * 2];
    public static int cnt;
    
    // 度数
    public static int[] degree = new int[MAXN];
    
    // 超级源点和超级汇点
    public static int S, T;
    
    // 网络流相关变量
    // 这里省略网络流的具体实现，因为涉及较多代码
    
    public static int n, m;
    
    public static void prepare() {
        cnt = 1;
        Arrays.fill(head, 1, n + 1, 0);
        Arrays.fill(degree, 1, n + 1, 0);
        S = 0;  // 超级源点
        T = n + 1; // 超级汇点
    }
    
    public static void addEdge(int u, int v) {
        // 无向图添加双向边
        next[cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt++;
        
        next[cnt] = head[v];
        to[cnt] = u;
        head[v] = cnt++;
        
        degree[u]++;
        degree[v]++;
    }
    
    // 检查是否存在密度大于x的子图
    // 这需要构建网络流模型并求解最大流
    // 由于网络流实现较为复杂，这里只给出框架
    public static boolean check(double x) {
        /*
         * 构造网络流模型：
         * 1. 每个点i拆成i和i'两个点
         * 2. S向每个点i连容量为m的边
         * 3. 每个点i向T连容量为2*x+m-degree[i]的边
         * 4. 原图中的每条边(i,j)，连接i'到j'和j'到i'，容量为1
         * 5. 每个点i连接到i'，容量为无穷大
         * 
         * 如果最大流 < m*n，则存在密度大于x的子图
         */
        
        // 实际实现需要网络流算法，此处省略具体代码
        // 返回示例值
        return true;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        while (true) {
            if (in.nextToken() == StreamTokenizer.TT_EOF) break;
            n = (int) in.nval;
            in.nextToken();
            m = (int) in.nval;
            
            if (n == 0 && m == 0) break;
            
            prepare();
            
            for (int i = 1; i <= m; i++) {
                in.nextToken();
                int u = (int) in.nval;
                in.nextToken();
                int v = (int) in.nval;
                addEdge(u, v);
            }
            
            // 特殊情况：如果没有边，密度为0
            if (m == 0) {
                out.println("0");
                continue;
            }
            
            // 二分答案求解最大密度
            double l = 0, r = m, ans = 0;
            while (r - l >= sml) {
                double x = (l + r) / 2;
                if (check(x)) {
                    ans = x;
                    l = x + sml;
                } else {
                    r = x - sml;
                }
            }
            
            // 输出结果
            out.printf("%.8f\n", ans);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}