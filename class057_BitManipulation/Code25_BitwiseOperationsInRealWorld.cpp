#include <iostream>
#include <vector>
#include <string>
#include <cmath>
#include <algorithm>
#include <stdexcept>
using namespace std;

/**
 * 位运算在实际工程中的应用
 * 测试链接：综合题目，展示位运算在真实工程场景中的应用
 * 
 * 题目描述：
 * 本文件展示位运算在各种实际工程场景中的应用，包括权限系统、状态压缩、加密算法等。
 * 
 * 解题思路：
 * 通过具体案例展示位运算如何解决实际问题，体现其高效性和实用性。
 * 
 * 时间复杂度：各种应用的时间复杂度不同，但通常为O(1)或O(log n)
 * 空间复杂度：O(1) - 通常只使用常数个变量
 */
class BitwiseOperationsInRealWorld {
public:
    // ==================== 权限系统应用 ====================
    
    /**
     * 权限系统：使用位掩码表示用户权限
     */
    class PermissionSystem {
    public:
        // 权限定义
        static const int READ = 1 << 0;    // 0001 - 读权限
        static const int WRITE = 1 << 1;   // 0010 - 写权限  
        static const int EXECUTE = 1 << 2; // 0100 - 执行权限
        static const int DELETE = 1 << 3;   // 1000 - 删除权限
        
        /**
         * 添加权限
         */
        static int addPermission(int current, int permission) {
            return current | permission;
        }
        
        /**
         * 移除权限
         */
        static int removePermission(int current, int permission) {
            return current & ~permission;
        }
        
        /**
         * 检查是否有权限
         */
        static bool hasPermission(int current, int permission) {
            return (current & permission) == permission;
        }
        
        /**
         * 切换权限（有则移除，无则添加）
         */
        static int togglePermission(int current, int permission) {
            return current ^ permission;
        }
        
        /**
         * 获取所有权限列表
         */
        static vector<string> getPermissionNames(int permissions) {
            vector<string> result;
            if (hasPermission(permissions, READ)) result.push_back("READ");
            if (hasPermission(permissions, WRITE)) result.push_back("WRITE");
            if (hasPermission(permissions, EXECUTE)) result.push_back("EXECUTE");
            if (hasPermission(permissions, DELETE)) result.push_back("DELETE");
            return result;
        }
    };
    
    // ==================== 状态压缩应用 ====================
    
    /**
     * 状态压缩：使用一个整数表示多个布尔状态
     */
    class StateCompression {
    public:
        /**
         * 设置状态位
         */
        static int setState(int state, int position, bool value) {
            if (value) {
                return state | (1 << position);
            } else {
                return state & ~(1 << position);
            }
        }
        
        /**
         * 获取状态位
         */
        static bool getState(int state, int position) {
            return (state & (1 << position)) != 0;
        }
        
        /**
         * 切换状态位
         */
        static int toggleState(int state, int position) {
            return state ^ (1 << position);
        }
        
        /**
         * 检查状态位模式
         */
        static bool checkPattern(int state, int pattern) {
            return (state & pattern) == pattern;
        }
    };
    
    // ==================== 颜色操作应用 ====================
    
    /**
     * 颜色操作：使用位运算处理RGB颜色
     */
    class ColorOperations {
    public:
        /**
         * 从RGB值创建颜色整数
         */
        static int createColor(int red, int green, int blue) {
            return (red << 16) | (green << 8) | blue;
        }
        
        /**
         * 从颜色整数提取红色分量
         */
        static int getRed(int color) {
            return (color >> 16) & 0xFF;
        }
        
        /**
         * 从颜色整数提取绿色分量
         */
        static int getGreen(int color) {
            return (color >> 8) & 0xFF;
        }
        
        /**
         * 从颜色整数提取蓝色分量
         */
        static int getBlue(int color) {
            return color & 0xFF;
        }
        
        /**
         * 调整颜色亮度（乘以系数）
         */
        static int adjustBrightness(int color, float factor) {
            int red = min(255, (int)(getRed(color) * factor));
            int green = min(255, (int)(getGreen(color) * factor));
            int blue = min(255, (int)(getBlue(color) * factor));
            return createColor(red, green, blue);
        }
        
        /**
         * 混合两种颜色
         */
        static int blendColors(int color1, int color2, float ratio) {
            float inverseRatio = 1.0f - ratio;
            int red = (int)(getRed(color1) * ratio + getRed(color2) * inverseRatio);
            int green = (int)(getGreen(color1) * ratio + getGreen(color2) * inverseRatio);
            int blue = (int)(getBlue(color1) * ratio + getBlue(color2) * inverseRatio);
            return createColor(red, green, blue);
        }
    };
    
    // ==================== 性能优化应用 ====================
    
    /**
     * 性能优化：使用位运算替代算术运算
     */
    class PerformanceOptimization {
    public:
        /**
         * 快速计算2的幂
         */
        static int powerOfTwo(int n) {
            return 1 << n;
        }
        
        /**
         * 快速判断是否是2的幂
         */
        static bool isPowerOfTwo(int n) {
            return n > 0 && (n & (n - 1)) == 0;
        }
        
        /**
         * 快速计算绝对值
         */
        static int abs(int n) {
            int mask = n >> 31;
            return (n + mask) ^ mask;
        }
        
        /**
         * 快速计算模2的幂
         */
        static int modPowerOfTwo(int n, int mod) {
            return n & (mod - 1);
        }
        
        /**
         * 快速交换两个数
         */
        static void swap(int arr[], int i, int j) {
            if (i != j) {
                arr[i] ^= arr[j];
                arr[j] ^= arr[i];
                arr[i] ^= arr[j];
            }
        }
    };
    
    // ==================== 数据结构优化应用 ====================
    
    /**
     * 位集（BitSet）简化实现
     */
    class CompactBitSet {
    private:
        vector<int> data;
        int size;
        
    public:
        CompactBitSet(int size) : size(size) {
            data.resize((size + 31) / 32, 0);
        }
        
        void set(int index) {
            if (index < 0 || index >= size) {
                throw out_of_range("Index out of bounds");
            }
            int arrayIndex = index / 32;
            int bitIndex = index % 32;
            data[arrayIndex] |= (1 << bitIndex);
        }
        
        void clear(int index) {
            if (index < 0 || index >= size) {
                throw out_of_range("Index out of bounds");
            }
            int arrayIndex = index / 32;
            int bitIndex = index % 32;
            data[arrayIndex] &= ~(1 << bitIndex);
        }
        
        bool get(int index) {
            if (index < 0 || index >= size) {
                throw out_of_range("Index out of bounds");
            }
            int arrayIndex = index / 32;
            int bitIndex = index % 32;
            return (data[arrayIndex] & (1 << bitIndex)) != 0;
        }
        
        int cardinality() {
            int count = 0;
            for (int value : data) {
                // 计算1的个数
                while (value != 0) {
                    value &= (value - 1);
                    count++;
                }
            }
            return count;
        }
    };
};

// ==================== 测试函数 ====================

int main() {
    cout << "=== 权限系统测试 ===" << endl;
    int userPermissions = 0;
    userPermissions = BitwiseOperationsInRealWorld::PermissionSystem::addPermission(
        userPermissions, BitwiseOperationsInRealWorld::PermissionSystem::READ);
    userPermissions = BitwiseOperationsInRealWorld::PermissionSystem::addPermission(
        userPermissions, BitwiseOperationsInRealWorld::PermissionSystem::WRITE);
    
    vector<string> permissionNames = BitwiseOperationsInRealWorld::PermissionSystem::getPermissionNames(userPermissions);
    cout << "用户权限: ";
    for (const string& name : permissionNames) {
        cout << name << " ";
    }
    cout << endl;
    
    cout << "有写权限: " << BitwiseOperationsInRealWorld::PermissionSystem::hasPermission(
        userPermissions, BitwiseOperationsInRealWorld::PermissionSystem::WRITE) << endl;
    
    cout << "\n=== 状态压缩测试 ===" << endl;
    int gameState = 0;
    gameState = BitwiseOperationsInRealWorld::StateCompression::setState(gameState, 0, true);  // 玩家存活
    gameState = BitwiseOperationsInRealWorld::StateCompression::setState(gameState, 1, true);  // 游戏进行中
    cout << "玩家存活: " << BitwiseOperationsInRealWorld::StateCompression::getState(gameState, 0) << endl;
    cout << "游戏进行中: " << BitwiseOperationsInRealWorld::StateCompression::getState(gameState, 1) << endl;
    
    cout << "\n=== 颜色操作测试 ===" << endl;
    int redColor = BitwiseOperationsInRealWorld::ColorOperations::createColor(255, 0, 0);
    int greenColor = BitwiseOperationsInRealWorld::ColorOperations::createColor(0, 255, 0);
    int blendedColor = BitwiseOperationsInRealWorld::ColorOperations::blendColors(redColor, greenColor, 0.5f);
    cout << "混合颜色 - R:" << BitwiseOperationsInRealWorld::ColorOperations::getRed(blendedColor) 
         << " G:" << BitwiseOperationsInRealWorld::ColorOperations::getGreen(blendedColor) 
         << " B:" << BitwiseOperationsInRealWorld::ColorOperations::getBlue(blendedColor) << endl;
    
    cout << "\n=== 性能优化测试 ===" << endl;
    int array[2] = {5, 3};
    BitwiseOperationsInRealWorld::PerformanceOptimization::swap(array, 0, 1);
    cout << "交换后: [" << array[0] << ", " << array[1] << "]" << endl;
    cout << "8是2的幂: " << BitwiseOperationsInRealWorld::PerformanceOptimization::isPowerOfTwo(8) << endl;
    cout << "15 mod 8: " << BitwiseOperationsInRealWorld::PerformanceOptimization::modPowerOfTwo(15, 8) << endl;
    
    cout << "\n=== 数据结构优化测试 ===" << endl;
    BitwiseOperationsInRealWorld::CompactBitSet bitSet(100);
    bitSet.set(42);
    bitSet.set(57);
    cout << "位42已设置: " << bitSet.get(42) << endl;
    cout << "位43未设置: " << bitSet.get(43) << endl;
    cout << "位集基数: " << bitSet.cardinality() << endl;
    
    cout << "\n=== 工程化考量总结 ===" << endl;
    cout << "1. 边界条件处理：所有方法都应处理边界情况" << endl;
    cout << "2. 性能优化：位运算通常比算术运算更快" << endl;
    cout << "3. 可读性：添加详细注释说明位运算原理" << endl;
    cout << "4. 错误处理：输入验证和异常处理" << endl;
    cout << "5. 测试覆盖：包含各种边界情况和特殊输入" << endl;
    
    return 0;
}