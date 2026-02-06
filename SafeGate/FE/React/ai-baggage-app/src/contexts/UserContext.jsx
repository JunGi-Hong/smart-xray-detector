//context이용해 사용자 정보 전역 상태로 관리

import { createContext, useContext } from 'react';

export const UserContext = createContext(null);

export const useUser = () => useContext(UserContext);