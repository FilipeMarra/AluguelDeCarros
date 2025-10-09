"use client"

import { useState, useEffect, createContext, useContext, ReactNode } from "react"

interface AuthContextType {
  token: string | null
  user: User | null
  login: (email: string, senha: string) => Promise<boolean>
  logout: () => void
  isAuthenticated: boolean
  isLoading: boolean
}

interface User {
  nome: string
  email: string
  role: "CLIENTE" | "AGENTE"
}

interface LoginResponse {
  token: string
  role: "CLIENTE" | "AGENTE"
  message: string
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(null)
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    // Verificar se há token salvo no localStorage
    const savedToken = localStorage.getItem("auth_token")
    const savedUser = localStorage.getItem("auth_user")
    
    if (savedToken && savedUser) {
      setToken(savedToken)
      setUser(JSON.parse(savedUser))
    }
    setIsLoading(false)
  }, [])

  const login = async (email: string, senha: string): Promise<boolean> => {
    try {
      const response = await fetch("http://localhost:8081/auth", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, senha }),
      })

      if (!response.ok) {
        return false
      }

      const data: LoginResponse = await response.json()
      
      if (data.token) {
        const userData: User = {
          nome: data.message,
          email: email,
          role: data.role,
        }

        setToken(data.token)
        setUser(userData)
        
        // Salvar no localStorage
        localStorage.setItem("auth_token", data.token)
        localStorage.setItem("auth_user", JSON.stringify(userData))
        
        return true
      }
      
      return false
    } catch (error) {
      console.error("Erro no login:", error)
      return false
    }
  }

  const logout = () => {
    setToken(null)
    setUser(null)
    localStorage.removeItem("auth_token")
    localStorage.removeItem("auth_user")
  }

  const value: AuthContextType = {
    token,
    user,
    login,
    logout,
    isAuthenticated: !!token,
    isLoading,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error("useAuth deve ser usado dentro de um AuthProvider")
  }
  return context
}

// Hook para obter headers de autenticação
export function useAuthHeaders() {
  const { token } = useAuth()
  
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  }
}
