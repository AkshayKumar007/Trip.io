interface ProgressIndicatorProps {
  steps: string[]
  currentStep: number
  completedSteps?: number[]
}

export function ProgressIndicator({ steps, currentStep, completedSteps = [] }: ProgressIndicatorProps) {
  return (
    <div className="flex items-center justify-between mb-6">
      {steps.map((step, index) => {
        const isCompleted = completedSteps.includes(index)
        const isCurrent = index === currentStep
        const isUpcoming = index > currentStep

        return (
          <div key={step} className="flex items-center">
            <div className="flex flex-col items-center">
              <div
                className={`
                w-8 h-8 rounded-full flex items-center justify-center text-xs font-medium
                ${isCompleted ? "bg-green-500 text-white" : ""}
                ${isCurrent ? "bg-[#3d99f5] text-white" : ""}
                ${isUpcoming ? "bg-[#e5e8eb] text-[#4a739c]" : ""}
              `}
              >
                {index + 1}
              </div>
              <span className="text-xs text-[#4a739c] mt-1 text-center max-w-16">{step}</span>
            </div>

            {index < steps.length - 1 && (
              <div
                className={`
                w-12 h-0.5 mx-2 mt-4
                ${index < currentStep ? "bg-green-500" : "bg-[#e5e8eb]"}
              `}
              />
            )}
          </div>
        )
      })}
    </div>
  )
}
