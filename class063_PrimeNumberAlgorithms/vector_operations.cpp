// 向量运算的C++实现
// 包括：点积、叉积、线性相关性判定
// 时间复杂度：
// - 点积/叉积：O(n)
// - 线性相关性判定：O(n³)
// 空间复杂度：O(n²)

#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <iomanip>
using namespace std;

// 三维向量类
class Vector3D {
public:
    double x, y, z;
    
    Vector3D(double x = 0, double y = 0, double z = 0) : x(x), y(y), z(z) {}
    
    // 向量加法
    Vector3D operator+(const Vector3D& v) const {
        return Vector3D(x + v.x, y + v.y, z + v.z);
    }
    
    // 向量减法
    Vector3D operator-(const Vector3D& v) const {
        return Vector3D(x - v.x, y - v.y, z - v.z);
    }
    
    // 标量乘法
    Vector3D operator*(double scalar) const {
        return Vector3D(x * scalar, y * scalar, z * scalar);
    }
    
    // 向量长度
    double length() const {
        return sqrt(x*x + y*y + z*z);
    }
    
    // 单位向量
    Vector3D normalize() const {
        double len = length();
        if (len < 1e-9) return Vector3D(); // 避免除以零
        return Vector3D(x/len, y/len, z/len);
    }
    
    // 打印向量
    void print() const {
        cout << "(" << x << ", " << y << ", " << z << ")";
    }
};

// 计算点积
// 时间复杂度：O(1)
double dot_product(const Vector3D& a, const Vector3D& b) {
    return a.x * b.x + a.y * b.y + a.z * b.z;
}

// 计算叉积
// 时间复杂度：O(1)
Vector3D cross_product(const Vector3D& a, const Vector3D& b) {
    return Vector3D(
        a.y * b.z - a.z * b.y,
        a.z * b.x - a.x * b.z,
        a.x * b.y - a.y * b.x
    );
}

// 计算向量夹角（弧度）
// 时间复杂度：O(1)
double angle_between(const Vector3D& a, const Vector3D& b) {
    double dot = dot_product(a, b);
    double len_product = a.length() * b.length();
    if (len_product < 1e-9) return 0;
    double cos_theta = dot / len_product;
    // 确保cos_theta在[-1, 1]范围内
    cos_theta = max(min(cos_theta, 1.0), -1.0);
    return acos(cos_theta);
}

// 通用向量类（任意维度）
class Vector {
public:
    vector<double> data;
    
    Vector(size_t dim = 0) : data(dim, 0) {}
    
    Vector(const vector<double>& v) : data(v) {}
    
    // 获取维度
    size_t dimension() const {
        return data.size();
    }
    
    // 访问元素
    double& operator[](size_t i) {
        return data[i];
    }
    
    double operator[](size_t i) const {
        return data[i];
    }
    
    // 向量加法
    Vector operator+(const Vector& v) const {
        size_t n = dimension();
        Vector result(n);
        for (size_t i = 0; i < n; i++) {
            result[i] = data[i] + v[i];
        }
        return result;
    }
    
    // 向量减法
    Vector operator-(const Vector& v) const {
        size_t n = dimension();
        Vector result(n);
        for (size_t i = 0; i < n; i++) {
            result[i] = data[i] - v[i];
        }
        return result;
    }
    
    // 标量乘法
    Vector operator*(double scalar) const {
        size_t n = dimension();
        Vector result(n);
        for (size_t i = 0; i < n; i++) {
            result[i] = data[i] * scalar;
        }
        return result;
    }
    
    // 向量长度
    double length() const {
        double sum = 0;
        for (double x : data) {
            sum += x * x;
        }
        return sqrt(sum);
    }
    
    // 打印向量
    void print() const {
        cout << "(";
        for (size_t i = 0; i < data.size(); i++) {
            cout << data[i];
            if (i < data.size() - 1) cout << ", ";
        }
        cout << ")";
    }
};

// 计算两个任意维度向量的点积
// 时间复杂度：O(n)
double dot_product(const Vector& a, const Vector& b) {
    if (a.dimension() != b.dimension()) {
        throw runtime_error("向量维度不匹配");
    }
    double sum = 0;
    for (size_t i = 0; i < a.dimension(); i++) {
        sum += a[i] * b[i];
    }
    return sum;
}

// 高斯消元法判断向量组的线性相关性
// 时间复杂度：O(n³)
bool is_linearly_dependent(const vector<Vector>& vectors) {
    if (vectors.empty()) return false;
    
    size_t m = vectors.size();      // 向量数量
    size_t n = vectors[0].dimension(); // 向量维度
    
    // 确保所有向量维度相同
    for (const auto& v : vectors) {
        if (v.dimension() != n) {
            throw runtime_error("向量维度不一致");
        }
    }
    
    // 构造增广矩阵进行高斯消元
    vector<vector<double>> mat(m, vector<double>(n, 0));
    for (size_t i = 0; i < m; i++) {
        for (size_t j = 0; j < n; j++) {
            mat[i][j] = vectors[i][j];
        }
    }
    
    size_t rank = 0;
    for (size_t col = 0; col < n && rank < m; col++) {
        // 寻找主元
        size_t pivot = rank;
        for (size_t i = rank; i < m; i++) {
            if (abs(mat[i][col]) > abs(mat[pivot][col])) {
                pivot = i;
            }
        }
        
        // 如果主元为零，继续下一列
        if (abs(mat[pivot][col]) < 1e-9) {
            continue;
        }
        
        // 交换行
        swap(mat[rank], mat[pivot]);
        
        // 归一化主行
        double div = mat[rank][col];
        for (size_t j = col; j < n; j++) {
            mat[rank][j] /= div;
        }
        
        // 消去其他行
        for (size_t i = 0; i < m; i++) {
            if (i != rank && abs(mat[i][col]) > 1e-9) {
                double factor = mat[i][col];
                for (size_t j = col; j < n; j++) {
                    mat[i][j] -= factor * mat[rank][j];
                }
            }
        }
        
        rank++;
    }
    
    // 如果秩小于向量数量，则线性相关
    return rank < m;
}

// 计算三个点是否共线
bool are_collinear(const Vector3D& a, const Vector3D& b, const Vector3D& c) {
    Vector3D ab = b - a;
    Vector3D ac = c - a;
    Vector3D cross = cross_product(ab, ac);
    // 如果叉积的长度接近零，则三点共线
    return cross.length() < 1e-9;
}

// 计算四个点是否共面
bool are_coplanar(const Vector3D& a, const Vector3D& b, const Vector3D& c, const Vector3D& d) {
    Vector3D ab = b - a;
    Vector3D ac = c - a;
    Vector3D ad = d - a;
    // 计算混合积，若为零则共面
    double triple_product = dot_product(ab, cross_product(ac, ad));
    return abs(triple_product) < 1e-9;
}

// 打印向量列表
void print_vectors(const vector<Vector>& vectors, const string& name) {
    cout << name << ":\n";
    for (const auto& v : vectors) {
        v.print();
        cout << endl;
    }
}

// 主函数 - 测试代码
int main() {
    // 测试三维向量运算
    cout << "=== 三维向量运算测试 ===" << endl;
    Vector3D a(1, 2, 3);
    Vector3D b(4, 5, 6);
    
    cout << "向量a: "; a.print(); cout << endl;
    cout << "向量b: "; b.print(); cout << endl;
    
    cout << "点积 a·b = " << dot_product(a, b) << endl;
    
    Vector3D cross = cross_product(a, b);
    cout << "叉积 a×b = "; cross.print(); cout << endl;
    
    double angle = angle_between(a, b);
    cout << "夹角 θ = " << angle << " 弧度 = " << angle * 180 / M_PI << " 度" << endl;
    
    // 测试共线性和共面性
    Vector3D c(2, 4, 6); // c = 2a，应该与a和b共面
    cout << "\n点a, b, c共线？ " << (are_collinear(a, b, c) ? "是" : "否") << endl;
    
    Vector3D d(7, 8, 9);
    cout << "点a, b, c, d共面？ " << (are_coplanar(a, b, c, d) ? "是" : "否") << endl;
    
    // 测试线性相关性
    cout << "\n=== 线性相关性测试 ===" << endl;
    
    // 线性相关的向量组
    vector<Vector> dependent_vectors;
    dependent_vectors.push_back(Vector({1, 2, 3}));
    dependent_vectors.push_back(Vector({4, 5, 6}));
    dependent_vectors.push_back(Vector({2, 3, 4})); // 这三个向量线性相关
    
    print_vectors(dependent_vectors, "线性相关向量组");
    cout << "线性相关？ " << (is_linearly_dependent(dependent_vectors) ? "是" : "否") << endl;
    
    // 线性无关的向量组
    vector<Vector> independent_vectors;
    independent_vectors.push_back(Vector({1, 0, 0}));
    independent_vectors.push_back(Vector({0, 1, 0}));
    independent_vectors.push_back(Vector({0, 0, 1})); // 这三个向量线性无关
    
    print_vectors(independent_vectors, "线性无关向量组");
    cout << "线性相关？ " << (is_linearly_dependent(independent_vectors) ? "是" : "否") << endl;
    
    /*
     * 算法解释：
     * 1. 向量运算包括点积、叉积、线性相关性判定等基本操作
     * 2. 三维向量有专门的实现，支持常见的几何运算
     * 3. 通用向量类支持任意维度的向量运算
     * 4. 线性相关性判定使用高斯消元法计算向量组的秩
     * 
     * 时间复杂度分析：
     * - 点积/叉积：O(n)，其中n是向量维度
     * - 线性相关性判定：O(m²n)，其中m是向量数量，n是向量维度
     * 
     * 应用场景：
     * 1. 计算几何中的点、线、面关系判断
     * 2. 机器学习中的特征向量分析
     * 3. 物理学中的力、速度、加速度计算
     * 4. 计算机图形学中的变换和渲染
     * 
     * 相关题目：
     * 1. LeetCode 1232. Check If It Is a Straight Line - 检查是否为直线
     * 2. 向量点积、叉积相关问题 - 几何计算
     * 3. 向量线性相关性问题 - 线性代数
     */
    
    return 0;
}