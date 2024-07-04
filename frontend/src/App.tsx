import {useEffect, useState} from 'react'
import './App.css'
import {BridgeApi} from 'bridge-api-client-ts'

enum UserType {
    ADMIN = 0,
    SELLER = 1,
    BUYER = 2,
}

interface User {
    id: number;
    name: string;
    age: number;
    type: UserType;
}

interface ApiCommonResponse<T> {
    data: T;
    message: string;
    status: number;
}

interface ErrorData {
    uniqueCode: string;
    message: string;
    errorType: number[];
    occurredAt: string;
}

function App() {
    const [result, setResult] = useState<string>('');
    const [userId, setUserId] = useState<string>('1');
    const [userName, setUserName] = useState<string>('');
    const [userAge, setUserAge] = useState<string>('');
    const [userType, setUserType] = useState<UserType>(UserType.BUYER);
    const [loading, setLoading] = useState<boolean>(false);

    useEffect(() => {
        const inputs = document.querySelectorAll('.input-section input, .input-section select');
        inputs.forEach((input: Element) => {
            (input as HTMLElement).style.opacity = '1';
        });
    }, []);

    const handleApiCall = async (apiCall: () => Promise<any>) => {
        setLoading(true);
        try {
            const response = await apiCall();
            setResult(JSON.stringify(response, null, 2));
        } catch (error) {
            setResult(`Error: ${(error as Error).message}`);
        } finally {
            setLoading(false);
        }
    }

    const customBridgeApi = BridgeApi.create({
        headers: {"Authorization": "Test Bearer token"},
        responseErrorHandler: (e: any) => {
            console.error(e)
            return Promise.reject(e);
        },
        timeout: 1,
    })

    const handleGetUser = () => handleApiCall(() => customBridgeApi.get<ApiCommonResponse<User>>(`/api/v1/users/${userId}`));

    const handleCreateUser = () => handleApiCall(() => customBridgeApi.post<ApiCommonResponse<User>>('/api/v1/users', {
        name: userName,
        age: parseInt(userAge),
        type: userType
    }));

    const handleUpdateUserType = () => handleApiCall(() => customBridgeApi.patch<ApiCommonResponse<User>>(`/api/v1/users/${userId}/user-type`, {
        name: userName,
        age: parseInt(userAge),
        type: userType
    }));

    const handleDeleteUser = () => handleApiCall(() => customBridgeApi.delete<ApiCommonResponse<null>>(`/api/v1/users/${userId}`));

    const handleGetAllUsers = () => handleApiCall(() => customBridgeApi.get<ApiCommonResponse<User[]>>('/api/v1/users/all?order=ASC'));

    const handleTestServiceException = () => handleApiCall(() => customBridgeApi.get<ApiCommonResponse<ErrorData>>('/api/v1/users/test/exception/service'));

    const handleTestGeneralException = () => {

        customBridgeApi.get<ApiCommonResponse<ErrorData>>('/api/v1/users/test/exception/general')
            .then(res => setResult(JSON.stringify(res, null, 2)))
    }

    const handleTestTimeout = async () => {
        setLoading(true); // 로딩 상태를 true로 설정
        await customBridgeApi.get<ApiCommonResponse<any>>('/api/v1/users/test/time-out')
            .then(res => {

                setResult(JSON.stringify(res, null, 2));
                setLoading(false);
            })
            .catch((e: any) => console.error(e))
            .finally(() => setLoading(false));


    }

    const handleTestSleep = () => {
        setLoading(true)
        const a = window.SleepApi.sleep()
        setResult(a)
        setLoading(false)
        // new Promise((resolve, _ ) => {
        //     resolve(window.SleepApi.sleep())
        // })
        //     .then((res: any) => setResult(JSON.stringify(res, null, 2)))
        //     .catch((e: any) => console.error(e))
        //     .finally(() => setLoading(false))


    }

    return (
        <div className="app">
            <h1>BridgeApi Test Dashboard</h1>
            <div className="input-section">
                <input
                    type="text"
                    value={userId}
                    onChange={(e) => setUserId(e.target.value)}
                    placeholder="User ID"
                />
                <input
                    type="text"
                    value={userName}
                    onChange={(e) => setUserName(e.target.value)}
                    placeholder="User Name"
                />
                <input
                    type="number"
                    value={userAge}
                    onChange={(e) => setUserAge(e.target.value)}
                    placeholder="User Age"
                />
                <select
                    value={userType}
                    onChange={(e) => setUserType(Number(e.target.value) as UserType)}
                >
                    <option value={UserType.ADMIN}>Admin</option>
                    <option value={UserType.SELLER}>Seller</option>
                    <option value={UserType.BUYER}>Buyer</option>
                </select>
            </div>
            <div className="button-section">
                <button onClick={handleGetUser} disabled={loading}>Get User</button>
                <button onClick={handleCreateUser} disabled={loading}>Create User</button>
                <button onClick={handleUpdateUserType} disabled={loading}>Update User Type</button>
                <button onClick={handleDeleteUser} disabled={loading}>Delete User</button>
                <button onClick={handleGetAllUsers} disabled={loading}>Get All Users</button>
                <button onClick={handleTestServiceException} disabled={loading}>Test Service Exception</button>
                <button onClick={handleTestGeneralException} disabled={loading}>Test General Exception</button>
                <button onClick={handleTestTimeout} disabled={loading}>Test Timeout</button>
                <button onClick={handleTestSleep} disabled={loading}>Test Sleep</button>
            </div>
            <div className="result-section">
                <h2>Result:</h2>
                {loading ? (
                    <div className="loader">Loading...</div>
                ) : (
                    <pre>{result}</pre>
                )}
            </div>
        </div>
    );
}

export default App;