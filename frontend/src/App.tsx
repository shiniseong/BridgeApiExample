import React, {useEffect, useState} from 'react'
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
    };

    const handleGetUser = () => handleApiCall(() => BridgeApi.get<ApiCommonResponse<User>>(`/api/v1/users/${userId}`));

    const handleCreateUser = () => handleApiCall(() => BridgeApi.post<ApiCommonResponse<User>>('/api/v1/users', {
        name: userName,
        age: parseInt(userAge),
        type: userType
    }));

    const handleUpdateUserType = () => handleApiCall(() => BridgeApi.patch<ApiCommonResponse<User>>(`/api/v1/users/${userId}/user-type`, {
        name: userName,
        age: parseInt(userAge),
        type: userType
    }));

    const handleDeleteUser = () => handleApiCall(() => BridgeApi.delete<ApiCommonResponse<null>>(`/api/v1/users/${userId}`));

    const handleGetAllUsers = () => handleApiCall(() => BridgeApi.get<ApiCommonResponse<User[]>>('/api/v1/users/all?order=ASC'));

    const handleTestServiceException = () => handleApiCall(() => BridgeApi.get<ApiCommonResponse<ErrorData>>('/api/v1/users/test/exception/service'));

    const handleTestGeneralException = () => handleApiCall(() => BridgeApi.get<ApiCommonResponse<ErrorData>>('/api/v1/users/test/exception/general'));

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