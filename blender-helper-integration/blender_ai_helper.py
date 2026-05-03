bl_info = {
    "name": "Blender AI Helper",
    "author": "Gagandeep",
    "version": (1, 0),
    "blender": (4, 0, 0),
    "location": "View3D > Sidebar",
    "description": "AI assistant for Blender workflows",
    "category": "3D View",
}

import bpy
import requests
import textwrap


class AIAskOperator(bpy.types.Operator):
    bl_idname = "ai.ask"
    bl_label = "Ask AI"

    def execute(self, context):

        user_input = context.scene.ai_prompt

        print("Captured input:", user_input)

        try:

            response = requests.post(
                "http://localhost:8080/chat",
                json={
                    "input": user_input
                }
            )

            result = response.json()

            context.scene.ai_response = result["reply"]

            print("Backend response:", result)

            self.report({'INFO'}, "Request successful")

        except Exception as e:

            print("Error:", e)

            context.scene.ai_response = "Backend request failed."

            self.report({'ERROR'}, "Request failed")

        return {'FINISHED'}


class AIHelperPanel(bpy.types.Panel):
    bl_label = "Blender AI Helper"
    bl_idname = "VIEW3D_PT_ai_helper"
    bl_space_type = 'VIEW_3D'
    bl_region_type = 'UI'
    bl_category = "AI Helper"

    def draw(self, context):

        layout = self.layout

        # Prompt input
        layout.prop(context.scene, "ai_prompt")

        # Ask button
        layout.operator("ai.ask")

        layout.separator()

        # Response section
        layout.label(text="AI Response:")

        box = layout.box()

        response_text = context.scene.ai_response

        if response_text:

            wrapped_lines = textwrap.wrap(
                response_text,
                width=40
            )

            for line in wrapped_lines:
                box.label(text=line)

        else:

            box.label(text="Waiting for response...")


classes = (
    AIHelperPanel,
    AIAskOperator
)


def register():

    bpy.types.Scene.ai_prompt = bpy.props.StringProperty(
        name="Prompt"
    )

    bpy.types.Scene.ai_response = bpy.props.StringProperty(
        name="AI Response",
        default=""
    )

    for cls in classes:
        bpy.utils.register_class(cls)


def unregister():

    del bpy.types.Scene.ai_prompt
    del bpy.types.Scene.ai_response

    for cls in reversed(classes):
        bpy.utils.unregister_class(cls)


if __name__ == "__main__":
    register()
