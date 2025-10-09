"use client"

import type React from "react"
import { useState } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

export function LoginForm() {
    const router = useRouter()
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [userType, setUserType] = useState<"cliente" | "agente">("cliente")
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState("")

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setIsLoading(true)
        setError("")

        try {
            const response = await fetch("http://localhost:8081/auth", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email,
                    senha: password,
                    userType, // ✅ Adicione esta linha
                }),
            })

            const data = await response.json()

            if (!response.ok) {
                setError(data.message || "Erro ao fazer login")
                setIsLoading(false)
                return
            }

            console.log("Login bem-sucedido:", data)
            localStorage.setItem("token", data.token)

            // Redirecionar baseado no tipo de usuário retornado pelo backend
            if (data.role === "CLIENTE") {
                router.push("/cliente")
            } else if (data.role === "AGENTE") {
                router.push("/agente")
            } else {
                setError("Tipo de usuário inválido")
            }
        } catch (err) {
            console.error(err)
            setError("Erro ao conectar com o servidor")
        } finally {
            setIsLoading(false)
        }
    }

    return (
        <Card className="w-full max-w-md">
            <CardHeader className="space-y-1">
                <CardTitle className="text-2xl font-bold">Entrar</CardTitle>
                <CardDescription>Digite suas credenciais para acessar o sistema</CardDescription>
            </CardHeader>
            <form onSubmit={handleSubmit}>
                <CardContent className="space-y-4">
                    <div className="space-y-2">
                        <Label htmlFor="userType">Tipo de Usuário</Label>
                        <Select value={userType} onValueChange={(value: "cliente" | "agente") => setUserType(value)}>
                            <SelectTrigger id="userType">
                                <SelectValue />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="cliente">Cliente</SelectItem>
                                <SelectItem value="agente">Agente</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                    <div className="space-y-2">
                        <Label htmlFor="email">Email</Label>
                        <Input
                            id="email"
                            type="email"
                            placeholder="seu@email.com"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className="space-y-2">
                        <div className="flex items-center justify-between">
                            <Label htmlFor="password">Senha</Label>
                            <Link href="/recuperar-senha" className="text-sm text-muted-foreground hover:text-primary">
                                Esqueceu a senha?
                            </Link>
                        </div>
                        <Input
                            id="password"
                            type="password"
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    {error && <p className="text-red-500 text-sm">{error}</p>}
                </CardContent>
                <CardFooter className="flex flex-col space-y-4">
                    <Button type="submit" className="w-full" disabled={isLoading}>
                        {isLoading ? "Entrando..." : "Entrar"}
                    </Button>
                    <p className="text-sm text-center text-muted-foreground">
                        Não tem uma conta?{" "}
                        <Link href="/registro" className="text-primary hover:underline font-medium">
                            Criar conta
                        </Link>
                    </p>
                </CardFooter>
            </form>
        </Card>
    )
}
