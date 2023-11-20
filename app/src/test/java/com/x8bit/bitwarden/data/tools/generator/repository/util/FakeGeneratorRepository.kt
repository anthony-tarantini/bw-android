package com.x8bit.bitwarden.data.tools.generator.repository.util

import com.bitwarden.core.PasswordGeneratorRequest
import com.x8bit.bitwarden.data.tools.generator.repository.GeneratorRepository
import com.x8bit.bitwarden.data.tools.generator.repository.model.GeneratedPasswordResult
import com.x8bit.bitwarden.data.tools.generator.repository.model.PasswordGenerationOptions

/**
 * A fake implementation of [GeneratorRepository] for testing purposes.
 * This class provides a simplified way to set up and control responses for repository methods.
 */
class FakeGeneratorRepository : GeneratorRepository {
    private var generatePasswordResult: GeneratedPasswordResult = GeneratedPasswordResult.Success(
        generatedString = "updatedText",
    )
    private var passwordGenerationOptions: PasswordGenerationOptions? = null

    override suspend fun generatePassword(
        passwordGeneratorRequest: PasswordGeneratorRequest,
    ): GeneratedPasswordResult {
        return generatePasswordResult
    }

    override fun getPasswordGenerationOptions(): PasswordGenerationOptions? {
        return passwordGenerationOptions
    }

    override fun savePasswordGenerationOptions(options: PasswordGenerationOptions) {
        passwordGenerationOptions = options
    }

    /**
     * Sets the mock result for the generatePassword function.
     */
    fun setMockGeneratePasswordResult(result: GeneratedPasswordResult) {
        generatePasswordResult = result
    }

    /**
     * Sets the mock password generation options.
     */
    fun setMockGeneratePasswordGenerationOptions(options: PasswordGenerationOptions?) {
        passwordGenerationOptions = options
    }
}
