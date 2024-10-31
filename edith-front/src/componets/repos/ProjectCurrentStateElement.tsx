interface ElementProps {
  colorCode: string;
  statusSubject: string;
  statusNum: number;
}
function ProjectCurrentStateElement({
  colorCode,
  statusSubject,
  statusNum,
}: ElementProps) {
  return (
    <>
      <div className="h-12 px-4 py-2 bg-white/30 rounded-3xl justify-center items-center gap-4 inline-flex">
        <div
          className={`w-11 h-11 rounded-full justify-center items-center gap-2.5 flex`}
          style={{ backgroundColor: colorCode }}
        >
          <div className="text-center text-black text-base font-semibold">
            {statusNum}
          </div>
        </div>
        <div className="text-center text-black font-medium">
          {statusSubject}
        </div>
      </div>
    </>
  );
}
export default ProjectCurrentStateElement;
