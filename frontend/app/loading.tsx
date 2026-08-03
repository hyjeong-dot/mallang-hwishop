import LoadingCharacter from '@/components/common/LoadingCharacter/LoadingCharacter';

export default function Loading() {
    return (
        <div style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            minHeight: '80vh'
        }}>
            <LoadingCharacter
                message="귀여운 상품들을 준비하고 있어요... 🎀"
                size={320}
            />
        </div>
    );
}
