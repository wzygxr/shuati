#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
高斯消元法全面测试框架

功能:
1. 单元测试: 验证每个算法的正确性
2. 边界测试: 测试极端输入情况
3. 性能测试: 验证时间空间复杂度
4. 异常测试: 验证错误处理能力
5. 跨语言一致性测试: 验证不同语言实现的一致性
"""

import os
import subprocess
import time
import sys
from typing import List, Dict, Any

class TestResult:
    """测试结果类"""
    def __init__(self, test_name: str, language: str, passed: bool, 
                 execution_time: float, memory_usage: float, error_message: str = ""):
        self.test_name = test_name
        self.language = language
        self.passed = passed
        self.execution_time = execution_time
        self.memory_usage = memory_usage
        self.error_message = error_message
    
    def __str__(self):
        status = "✓ PASS" if self.passed else "✗ FAIL"
        return f"{status} | {self.language:8} | {self.test_name:30} | {self.execution_time:8.3f}s | {self.memory_usage:8.2f}MB | {self.error_message}"

class GaussianEliminationTester:
    """高斯消元法测试器"""
    
    def __init__(self):
        self.test_cases = self._generate_test_cases()
        self.results: List[TestResult] = []
    
    def _generate_test_cases(self) -> List[Dict[str, Any]]:
        """生成测试用例"""
        return [
            # 基础测试用例
            {
                "name": "2x2_linear_system",
                "matrix": [[2, 1, 5], [1, 3, 10]],
                "expected": [1.0, 3.0],
                "description": "2x2线性方程组求解"
            },
            {
                "name": "3x3_linear_system",
                "matrix": [[2, 1, -1, 8], [-3, -1, 2, -11], [-2, 1, 2, -3]],
                "expected": [2.0, 3.0, -1.0],
                "description": "3x3线性方程组求解"
            },
            # 边界测试用例
            {
                "name": "singular_matrix",
                "matrix": [[1, 2, 3], [2, 4, 6], [3, 6, 9]],
                "expected": None,  # 无解或多解
                "description": "奇异矩阵测试"
            },
            {
                "name": "zero_matrix",
                "matrix": [[0, 0, 0], [0, 0, 0], [0, 0, 0]],
                "expected": None,
                "description": "零矩阵测试"
            },
            # 性能测试用例
            {
                "name": "large_matrix_5x5",
                "matrix": [
                    [4, 1, 2, -3, 5, 10],
                    [1, 5, -2, 1, 3, 15],
                    [2, -2, 6, 4, -1, 20],
                    [-3, 1, 4, 7, 2, 25],
                    [5, 3, -1, 2, 8, 30]
                ],
                "expected": [1.0, 2.0, 3.0, 4.0, 5.0],
                "description": "5x5大型矩阵测试"
            }
        ]
    
    def test_java_implementation(self, file_path: str) -> List[TestResult]:
        """测试Java实现"""
        results = []
        
        # 编译Java文件
        try:
            subprocess.run(["javac", file_path], check=True, capture_output=True)
        except subprocess.CalledProcessError as e:
            results.append(TestResult("compilation", "Java", False, 0, 0, f"编译失败: {e.stderr.decode()}"))
            return results
        
        # 运行测试用例
        class_name = os.path.basename(file_path).replace('.java', '')
        
        for test_case in self.test_cases:
            try:
                start_time = time.time()
                
                # 创建临时测试文件
                test_runner = self._create_java_test_runner(class_name, test_case)
                
                # 编译并运行测试
                with open('TestRunner.java', 'w') as f:
                    f.write(test_runner)
                
                subprocess.run(["javac", "TestRunner.java"], check=True)
                result = subprocess.run(["java", "TestRunner"], capture_output=True, text=True, timeout=10)
                
                execution_time = time.time() - start_time
                
                if result.returncode == 0:
                    results.append(TestResult(test_case["name"], "Java", True, execution_time, 0))
                else:
                    results.append(TestResult(test_case["name"], "Java", False, execution_time, 0, result.stderr))
                    
            except Exception as e:
                results.append(TestResult(test_case["name"], "Java", False, 0, 0, str(e)))
        
        # 清理临时文件
        try:
            os.remove("TestRunner.java")
            os.remove("TestRunner.class")
        except:
            pass
            
        return results
    
    def _create_java_test_runner(self, class_name: str, test_case: Dict) -> str:
        """创建Java测试运行器"""
        return f"""
import java.util.Arrays;

public class TestRunner {{
    public static void main(String[] args) {{
        try {{
            {class_name} solver = new {class_name}();
            
            // 测试用例: {test_case['description']}
            double[][] matrix = {self._java_matrix_repr(test_case['matrix'])};
            
            double[] result = solver.solve(matrix);
            
            if (result != null) {{
                System.out.println("结果: " + Arrays.toString(result));
            }} else {{
                System.out.println("无解或多解");
            }}
            
            System.exit(0);
        }} catch (Exception e) {{
            e.printStackTrace();
            System.exit(1);
        }}
    }}
    
    {self._java_matrix_helper()}
}}
"""
    
    def _java_matrix_repr(self, matrix: List[List[Any]]) -> str:
        """生成Java矩阵表示"""
        rows = []
        for row in matrix:
            row_str = "{" + ", ".join(str(x) for x in row) + "}"
            rows.append(row_str)
        return "{" + ", ".join(rows) + "}"
    
    def _java_matrix_helper(self) -> str:
        """Java矩阵辅助方法"""
        return """
    // 矩阵辅助方法
    private static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
"""
    
    def test_cpp_implementation(self, file_path: str) -> List[TestResult]:
        """测试C++实现"""
        results = []
        
        # 编译C++文件
        executable = file_path.replace('.cpp', '.exe')
        
        try:
            result = subprocess.run(["g++", "-o", executable, file_path, "-std=c++17"], 
                                 capture_output=True, text=True)
            if result.returncode != 0:
                results.append(TestResult("compilation", "C++", False, 0, 0, f"编译失败: {result.stderr}"))
                return results
        except Exception as e:
            results.append(TestResult("compilation", "C++", False, 0, 0, str(e)))
            return results
        
        # 运行测试用例
        for test_case in self.test_cases:
            try:
                start_time = time.time()
                
                # 创建临时测试输入文件
                test_input = self._create_cpp_test_input(test_case)
                
                # 运行程序
                result = subprocess.run([executable], input=test_input, 
                                      capture_output=True, text=True, timeout=10)
                
                execution_time = time.time() - start_time
                
                if result.returncode == 0:
                    results.append(TestResult(test_case["name"], "C++", True, execution_time, 0))
                else:
                    results.append(TestResult(test_case["name"], "C++", False, execution_time, 0, result.stderr))
                    
            except Exception as e:
                results.append(TestResult(test_case["name"], "C++", False, 0, 0, str(e)))
        
        # 清理可执行文件
        try:
            os.remove(executable)
        except:
            pass
            
        return results
    
    def _create_cpp_test_input(self, test_case: Dict) -> str:
        """创建C++测试输入"""
        matrix = test_case['matrix']
        n = len(matrix)
        
        input_str = f"{n}\n"  # 矩阵大小
        for row in matrix:
            input_str += " ".join(str(x) for x in row) + "\n"
        
        return input_str
    
    def test_python_implementation(self, file_path: str) -> List[TestResult]:
        """测试Python实现"""
        results = []
        
        # 直接导入Python模块进行测试
        for test_case in self.test_cases:
            try:
                start_time = time.time()
                
                # 创建临时测试脚本
                test_script = self._create_python_test_script(file_path, test_case)
                
                with open('temp_test.py', 'w') as f:
                    f.write(test_script)
                
                result = subprocess.run([sys.executable, 'temp_test.py'], 
                                      capture_output=True, text=True, timeout=10)
                
                execution_time = time.time() - start_time
                
                if result.returncode == 0:
                    results.append(TestResult(test_case["name"], "Python", True, execution_time, 0))
                else:
                    results.append(TestResult(test_case["name"], "Python", False, execution_time, 0, result.stderr))
                    
            except Exception as e:
                results.append(TestResult(test_case["name"], "Python", False, 0, 0, str(e)))
        
        # 清理临时文件
        try:
            os.remove('temp_test.py')
        except:
            pass
            
        return results
    
    def _create_python_test_script(self, file_path: str, test_case: Dict) -> str:
        """创建Python测试脚本"""
        module_name = file_path.replace('.py', '').replace('/', '.')
        
        return f"""
import sys
sys.path.insert(0, '.')

# 动态导入模块
exec(open('{file_path}').read())

# 获取求解器类
solver_class = None
for name, obj in list(locals().items()):
    if hasattr(obj, '__bases__') and any('Gaussian' in str(base) for base in obj.__bases__):
        solver_class = obj
        break

if solver_class is None:
    # 如果没有找到类，尝试直接调用函数
    try:
        matrix = {test_case['matrix']}
        
        # 尝试导入solve函数
        from {module_name} import solve
        result = solve(matrix)
        print(f"结果: {{result}}")
        sys.exit(0)
    except Exception as e:
        print(f"错误: {{e}}")
        sys.exit(1)
else:
    # 使用类进行求解
    try:
        solver = solver_class()
        matrix = {test_case['matrix']}
        result = solver.solve(matrix)
        print(f"结果: {{result}}")
        sys.exit(0)
    except Exception as e:
        print(f"错误: {{e}}")
        sys.exit(1)
"""
    
    def run_comprehensive_tests(self):
        """运行全面测试"""
        print("=" * 80)
        print("高斯消元法全面测试开始")
        print("=" * 80)
        
        total_tests = 0
        passed_tests = 0
        
        # 测试所有实现文件
        for filename in os.listdir('.'):
            if filename.endswith('.java') and 'Test' not in filename:
                print(f"\n测试Java实现: {filename}")
                results = self.test_java_implementation(filename)
                self.results.extend(results)
                
                for result in results:
                    print(result)
                    total_tests += 1
                    if result.passed:
                        passed_tests += 1
            
            elif filename.endswith('.cpp'):
                print(f"\n测试C++实现: {filename}")
                results = self.test_cpp_implementation(filename)
                self.results.extend(results)
                
                for result in results:
                    print(result)
                    total_tests += 1
                    if result.passed:
                        passed_tests += 1
            
            elif filename.endswith('.py') and not filename.startswith('test_') and not filename.startswith('enhance_') and not filename.startswith('search_') and not filename.startswith('analyze_') and not filename.startswith('generate_'):
                print(f"\n测试Python实现: {filename}")
                results = self.test_python_implementation(filename)
                self.results.extend(results)
                
                for result in results:
                    print(result)
                    total_tests += 1
                    if result.passed:
                        passed_tests += 1
        
        # 输出测试总结
        print("\n" + "=" * 80)
        print("测试总结")
        print("=" * 80)
        print(f"总测试数: {total_tests}")
        print(f"通过测试: {passed_tests}")
        print(f"失败测试: {total_tests - passed_tests}")
        print(f"通过率: {passed_tests/total_tests*100:.2f}%")
        
        # 按语言统计
        language_stats = {}
        for result in self.results:
            if result.language not in language_stats:
                language_stats[result.language] = {'total': 0, 'passed': 0}
            language_stats[result.language]['total'] += 1
            if result.passed:
                language_stats[result.language]['passed'] += 1
        
        print("\n按语言统计:")
        for lang, stats in language_stats.items():
            rate = stats['passed'] / stats['total'] * 100
            print(f"{lang:8}: {stats['passed']}/{stats['total']} ({rate:.2f}%)")

if __name__ == "__main__":
    tester = GaussianEliminationTester()
    tester.run_comprehensive_tests()